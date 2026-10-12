package com.cse310.studentgradetracker.ui
import com.cse310.studentgradetracker.model.Student
import com.cse310.studentgradetracker.data.StudentRepository
import com.cse310.studentgradetracker.validation.InputValidator
import com.cse310.studentgradetracker.data.DatabaseErrorHandler





// ============================================================
// COMPOSE IMPORTS
// ============================================================

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


// ============================================================
// OUR APPLICATION IMPORTS
// ============================================================





/**
 * StudentsScreen
 *
 * This composable displays the Students section of our
 * Student Grade Tracker.
 *
 * The screen allows the user to:
 *
 * 1. Add students
 * 2. Retrieve students
 * 3. Edit students
 * 4. Delete students
 *
 * Those four operations correspond to CRUD:
 *
 * CREATE  -> INSERT
 * READ    -> SELECT
 * UPDATE  -> UPDATE
 * DELETE  -> DELETE
 */
@Composable
fun StudentsScreen() {


    // ============================================================
    // REPOSITORY
    // ============================================================

    /**
     * Create our StudentRepository.
     *
     * remember { }
     *
     * tells Compose to keep the same repository object when
     * the screen recomposes.
     *
     * Without remember, Compose could create another repository
     * every time the UI recomposes.
     */
    val repository = remember {
        StudentRepository()
    }


    // ============================================================
    // FORM STATE
    // ============================================================

    /**
     * Stores whatever the user types into the First Name field.
     *
     * mutableStateOf tells Compose:
     *
     * "If this value changes, update any UI that uses it."
     */
    var firstName by remember {
        mutableStateOf("")
    }


    /**
     * Stores the Last Name field.
     */
    var lastName by remember {
        mutableStateOf("")
    }


    /**
     * Stores the Email field.
     */
    var email by remember {
        mutableStateOf("")
    }


    var studentPendingDelete by remember {
        mutableStateOf<Student?>(null)
    }




    // ============================================================
    // EDIT STATE
    // ============================================================

    /**
     * Stores the ID of the student currently being edited.
     *
     * null means:
     *
     * We are creating a NEW student.
     *
     * A Long value means:
     *
     * We are editing an EXISTING student.
     *
     * Example:
     *
     * editingStudentId = null
     *
     *      -> Add mode
     *
     * editingStudentId = 4
     *
     *      -> Edit student #4
     */
    var editingStudentId by remember {
        mutableStateOf<Long?>(null)
    }


    // ============================================================
    // STUDENT LIST STATE
    // ============================================================

    /**
     * Load students from SQLite.
     *
     * getAllStudents() executes:
     *
     * SELECT id, first_name, last_name, email
     * FROM students ...
     *
     * SQLite returns rows.
     *
     * StudentRepository converts those rows into:
     *
     * List<Student>
     */
    var students by remember {
        mutableStateOf(repository.getAllStudents())
    }


    // ============================================================
    // MESSAGE STATE
    // ============================================================

    /**
     * Used to display simple success/error messages.
     */
    var message by remember {
        mutableStateOf("")
    }


    // ============================================================
    // HELPER FUNCTION
    // ============================================================

    /**
     * refreshStudents()
     *
     * Queries SQLite again and updates our Compose state.
     *
     * When students changes, Compose automatically redraws
     * the student list.
     */
    fun refreshStudents() {

        students = repository.getAllStudents()
    }


    /**
     * clearForm()
     *
     * Resets the input fields.
     *
     * It also exits edit mode.
     */
    fun clearForm() {

        firstName = ""
        lastName = ""
        email = ""

        editingStudentId = null
    }


    // ============================================================
    // SCREEN LAYOUT
    // ============================================================

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {


        // ========================================================
        // SCREEN TITLE
        // ========================================================

        Text(
            text = "Students",
            style = MaterialTheme.typography.headlineLarge
        )


        Text(
            text = "Add, edit, and manage students.",
            style = MaterialTheme.typography.bodyMedium
        )


        HorizontalDivider()


        // ========================================================
        // STUDENT FORM
        // ========================================================

        Text(
            text =
                if (editingStudentId == null)
                    "Add Student"
                else
                    "Edit Student",

            style = MaterialTheme.typography.headlineSmall
        )


        // --------------------------------------------------------
        // FIRST NAME
        // --------------------------------------------------------

        OutlinedTextField(

            value = firstName,

            onValueChange = {

                /*
                 * `it` represents the new text entered
                 * by the user.
                 */
                firstName = it
            },

            label = {
                Text("First Name")
            },

            singleLine = true,

            modifier = Modifier.fillMaxWidth()
        )


        // --------------------------------------------------------
        // LAST NAME
        // --------------------------------------------------------

        OutlinedTextField(

            value = lastName,

            onValueChange = {
                lastName = it
            },

            label = {
                Text("Last Name")
            },

            singleLine = true,

            modifier = Modifier.fillMaxWidth()
        )


        // --------------------------------------------------------
        // EMAIL
        // --------------------------------------------------------

        OutlinedTextField(

            value = email,

            onValueChange = {
                email = it
            },

            label = {
                Text("Email")
            },

            singleLine = true,

            modifier = Modifier.fillMaxWidth()
        )


        // ========================================================
        // BUTTONS
        // ========================================================

        Row(

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)

        ) {


            // ====================================================
            // ADD / SAVE BUTTON
            // ====================================================

            Button(

                onClick = {


                    // --------------------------------------------
                    // SIMPLE VALIDATION
                    // --------------------------------------------

                    if (
                        firstName.isBlank() ||
                        lastName.isBlank() ||
                        email.isBlank()
                    ) {

                        message =
                            "Please complete all student fields."

                    } else {


                        try {


                            // ====================================
                            // CREATE MODE
                            // ====================================

                            if (editingStudentId == null) {


                                /*
                                 * Build a Kotlin Student object
                                 * using the information entered
                                 * into the form.
                                 */
                                val student = Student(

                                    firstName =
                                        firstName.trim(),

                                    lastName =
                                        lastName.trim(),

                                    email =
                                        email.trim()
                                )


                                val validationError = InputValidator.validateStudent(
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email
                                )

                                if (validationError != null) {

                                    message = validationError

                                } else {

                                    try {

                                        // Keep your existing INSERT or UPDATE logic here.
                                        // Run refreshStudents() after the operation succeeds.
                                        // Run clearForm() after saving successfully.

                                    } catch (exception: Exception) {

                                        message = DatabaseErrorHandler.getMessage(exception)
                                    }
                                }






                                /*
                                 * Send the Student to our
                                 * repository.
                                 *
                                 * StudentRepository executes:
                                 *
                                 * INSERT INTO students ...
                                 */
                                repository.addStudent(student)


                                message =
                                    "Student added successfully."


                            } else {


                                // =================================
                                // UPDATE MODE
                                // =================================


                                /*
                                 * editingStudentId is nullable.
                                 *
                                 * Because we're inside the branch
                                 * where it isn't null, retrieve it.
                                 */
                                val studentId =
                                    editingStudentId


                                if (studentId != null) {


                                    /*
                                     * Create an updated Student.
                                     *
                                     * Unlike a new Student, we MUST
                                     * include the existing ID.
                                     */
                                    val updatedStudent =
                                        Student(

                                            id = studentId,

                                            firstName =
                                                firstName.trim(),

                                            lastName =
                                                lastName.trim(),

                                            email =
                                                email.trim()
                                        )


                                    /*
                                     * Executes SQL UPDATE.
                                     */
                                    repository.updateStudent(
                                        updatedStudent
                                    )


                                    message =
                                        "Student updated successfully."
                                }
                            }


                            /*
                             * Query the database again.
                             *
                             * This ensures the UI reflects what
                             * SQLite actually contains.
                             */
                            refreshStudents()


                            /*
                             * Clear the input fields.
                             */
                            clearForm()


                        } catch (exception: Exception) {


                            /*
                             * A common error here could be:
                             *
                             * UNIQUE constraint failed
                             *
                             * if the same email is entered twice.
                             */
                            message =
                                "Database error: ${exception.message}"
                        }
                    }
                }

            ) {

                /*
                 * Change button text depending on whether
                 * we're adding or editing.
                 */
                Text(

                    if (editingStudentId == null)
                        "Add Student"
                    else
                        "Save Changes"
                )
            }


            // ====================================================
            // CANCEL EDIT BUTTON
            // ====================================================

            if (editingStudentId != null) {

                OutlinedButton(

                    onClick = {

                        clearForm()

                        message =
                            "Edit cancelled."
                    }

                ) {

                    Text("Cancel")
                }
            }
        }


        // ========================================================
        // STATUS MESSAGE
        // ========================================================

        if (message.isNotBlank()) {

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }


        HorizontalDivider()


        // ========================================================
        // STUDENT LIST HEADER
        // ========================================================

        Text(
            text = "Student List",
            style = MaterialTheme.typography.headlineSmall
        )


        Text(
            text = "Total students: ${students.size}",
            style = MaterialTheme.typography.bodyMedium
        )


        // ========================================================
        // EMPTY DATABASE MESSAGE
        // ========================================================

        if (students.isEmpty()) {

            Text(
                text = "No students have been added yet."
            )

        } else {


            // ====================================================
            // STUDENT LIST
            // ====================================================

            LazyColumn(

                verticalArrangement =
                    Arrangement.spacedBy(8.dp),

                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)

            ) {


                /*
                 * `items` loops through our List<Student>.
                 *
                 * Each Student receives its own row.
                 */
                items(
                    items = students,
                    key = { student -> student.id }
                ) { student ->


                    StudentRow(

                        student = student,


                        // ========================================
                        // EDIT EVENT
                        // ========================================

                        onEdit = {


                            /*
                             * Put the selected student's
                             * information into the form.
                             */
                            editingStudentId =
                                student.id

                            firstName =
                                student.firstName

                            lastName =
                                student.lastName

                            email =
                                student.email


                            message =
                                "Editing ${student.firstName} ${student.lastName}"
                        },


                        // ========================================
                        // DELETE EVENT
                        // ========================================

                        onDelete = {


                            /*try {


                                *//*
                                 * Execute:
                                 *
                                 * DELETE FROM students
                                 * WHERE id = ?
                                 *//*
                                repository.deleteStudent(
                                    student.id
                                )


                                *//*
                                 * Reload database records.
                                 *//*
                                refreshStudents()


                                *//*
                                 * If we happen to delete the
                                 * student currently being edited,
                                 * clear the form too.
                                 *//*
                                if (
                                    editingStudentId ==
                                    student.id
                                ) {

                                    clearForm()
                                }


                                message =
                                    "Student deleted successfully."


                            } catch (exception: Exception) {

                                message =
                                    "Database error: ${exception.message}"
                            }
                       */

                            studentPendingDelete = student

                        }
                    )
                }
            }
        }
    }



// ========================================================
// DELETE CONFIRMATION
// ========================================================

    studentPendingDelete?.let { student ->

        ConfirmDeleteDialog(

            itemName = "${student.firstName} ${student.lastName}",

            onConfirm = {

                try {

                    // Execute SQL DELETE only after confirmation.
                    repository.deleteStudent(student.id)

                    // Retrieve the updated student list.
                    refreshStudents()

                    // Clear the form if editing this student.
                    if (editingStudentId == student.id) {
                        clearForm()
                    }

                    message = "Student deleted successfully."

                } catch (exception: Exception) {

                    message =
                        DatabaseErrorHandler.getMessage(exception)

                } finally {

                    // Close the dialog.
                    studentPendingDelete = null
                }
            },

            onDismiss = {

                // Cancel without changing the database.
                studentPendingDelete = null
            }
        )
    }


}


/**
 * StudentRow
 *
 * Displays ONE student retrieved from SQLite.
 *
 * Example:
 *
 * Alice Johnson
 * alice@school.edu
 *
 * [Edit] [Delete]
 */
@Composable
private fun StudentRow(

    student: Student,

    onEdit: () -> Unit,

    onDelete: () -> Unit
) {


    /**
     * Card gives each student a separate visual container.
     */
    Card(

        modifier = Modifier.fillMaxWidth()

    ) {


        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.SpaceBetween

        ) {


            // ====================================================
            // STUDENT INFORMATION
            // ====================================================

            Column(

                modifier = Modifier.weight(1f)

            ) {


                /*
                 * Display student's full name.
                 */
                Text(

                    text =
                        "${student.firstName} ${student.lastName}",

                    style =
                        MaterialTheme.typography.titleMedium
                )


                /*
                 * Display student's email.
                 */
                Text(

                    text = student.email,

                    style =
                        MaterialTheme.typography.bodyMedium
                )


                /*
                 * Display the SQLite primary key.
                 *
                 * This is useful while developing the project.
                 */
                Text(

                    text = "Student ID: ${student.id}",

                    style =
                        MaterialTheme.typography.bodySmall
                )
            }


            // ====================================================
            // ACTION BUTTONS
            // ====================================================

            Row(

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)

            ) {


                /*
                 * Tell StudentsScreen that this student
                 * should be edited.
                 */
                OutlinedButton(

                    onClick = onEdit

                ) {

                    Text("Edit")
                }


                /*
                 * Tell StudentsScreen that this student
                 * should be deleted.
                 */
                Button(

                    onClick = onDelete

                ) {

                    Text("Delete")
                }
            }
        }
    }
}
