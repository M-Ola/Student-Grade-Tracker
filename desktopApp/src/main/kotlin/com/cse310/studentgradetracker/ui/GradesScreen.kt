package com.cse310.studentgradetracker.ui


// ============================================================
// COMPOSE IMPORTS
// ============================================================

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ============================================================
// APPLICATION IMPORTS
// ============================================================

import com.cse310.studentgradetracker.data.StudentRepository
import com.cse310.studentgradetracker.data.CourseRepository
import com.cse310.studentgradetracker.data.GradeRepository

import com.cse310.studentgradetracker.model.Student
import com.cse310.studentgradetracker.model.Course
import com.cse310.studentgradetracker.model.Grade
import com.cse310.studentgradetracker.model.GradeDetails

import com.cse310.studentgradetracker.validation.InputValidator
import com.cse310.studentgradetracker.data.DatabaseErrorHandler

/**
 * GradesScreen
 *
 * Provides a graphical interface for:
 *
 * 1. Selecting a student.
 * 2. Selecting a course.
 * 3. Entering a grade.
 * 4. Inserting grades into SQLite.
 * 5. Displaying SQL JOIN results.
 * 6. Editing existing grades.
 * 7. Deleting grades.
 */
@Composable
fun GradesScreen() {

    // ========================================================
    // REPOSITORIES
    // ========================================================

    val studentRepository = remember {
        StudentRepository()
    }

    val courseRepository = remember {
        CourseRepository()
    }

    val gradeRepository = remember {
        GradeRepository()
    }

    // ========================================================
    // DATABASE RESULTS
    // ========================================================

    /*
     * These collections contain records retrieved
     * from SQLite.
     *
     * We use mutableStateOf so Compose can redraw
     * the screen when these values change.
     */
    var students by remember {
        mutableStateOf<List<Student>>(emptyList())
    }

    var courses by remember {
        mutableStateOf<List<Course>>(emptyList())
    }

    var grades by remember {
        mutableStateOf<List<Grade>>(emptyList())
    }

    var gradeDetails by remember {
        mutableStateOf<List<GradeDetails>>(emptyList())
    }

    // ========================================================
    // FORM STATE
    // ========================================================

    /*
     * Selected student and course are stored as IDs.
     *
     * This is important because the grades table
     * stores foreign keys, not student/course names.
     */
    var selectedStudentId by remember {
        mutableStateOf<Long?>(null)
    }

    var selectedCourseId by remember {
        mutableStateOf<Long?>(null)
    }

    var scoreText by remember {
        mutableStateOf("")
    }

    /*
     * null means Add mode.
     *
     * A non-null ID means Edit mode.
     */
    var editingGradeId by remember {
        mutableStateOf<Long?>(null)
    }

    var message by remember {
        mutableStateOf("")
    }

    var isError by remember {
        mutableStateOf(false)
    }

    /**
     * Stores the grade the user wants to delete.
     *
     * null means no confirmation dialog is open.
     */
    var gradePendingDelete by remember {
        mutableStateOf<GradeDetails?>(null)
    }



    // ========================================================
    // DATABASE HELPERS
    // ========================================================

    /**
     * Reload all database-backed collections.
     *
     * Most importantly, getGradeDetails() executes
     * the SQL JOIN from Increment 6.
     */
    fun refreshData() {

        students = studentRepository.getAllStudents()

        courses = courseRepository.getAllCourses()

        grades = gradeRepository.getAllGrades()

        gradeDetails = gradeRepository.getGradeDetails()
    }

    /**
     * Clear the form and return to Add mode.
     */
    fun clearForm() {

        selectedStudentId = null

        selectedCourseId = null

        scoreText = ""

        editingGradeId = null
    }

    /**
     * Show a message to the user.
     */
    fun showMessage(text: String, error: Boolean = false) {

        message = text

        isError = error
    }

    // ========================================================
    // INITIAL DATABASE LOAD
    // ========================================================

    /*
     * Runs when this screen enters composition.
     *
     * It loads existing records from SQLite.
     */
    LaunchedEffect(Unit) {

        try {

            refreshData()

        } catch (exception: Exception) {

            showMessage(
                "Unable to load grades: ${exception.message}",
                true
            )
        }
    }

    // ========================================================
    // SCREEN LAYOUT
    // ========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // ====================================================
        // HEADER
        // ====================================================

        Text(
            text = "Grades",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Record and manage student grades.",
            style = MaterialTheme.typography.bodyMedium
        )

        HorizontalDivider()

        // ====================================================
        // FORM
        // ====================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = if (editingGradeId == null)
                    "Add Grade"
                else
                    "Edit Grade",

                style = MaterialTheme.typography.headlineSmall
            )

            // ================================================
            // STUDENT DROPDOWN
            // ================================================

            EntityDropdown(
                label = "Student",

                selectedText = students
                    .firstOrNull { it.id == selectedStudentId }
                    ?.let { "${it.firstName} ${it.lastName}" }
                    ?: "Select Student",

                options = students.map {
                    it.id to "${it.firstName} ${it.lastName}"
                },

                onSelected = { id ->
                    selectedStudentId = id
                }
            )

            // ================================================
            // COURSE DROPDOWN
            // ================================================

            EntityDropdown(
                label = "Course",

                selectedText = courses
                    .firstOrNull { it.id == selectedCourseId }
                    ?.let { "${it.courseCode} — ${it.courseName}" }
                    ?: "Select Course",

                options = courses.map {
                    it.id to "${it.courseCode} — ${it.courseName}"
                },

                onSelected = { id ->
                    selectedCourseId = id
                }
            )

            // ================================================
            // SCORE INPUT
            // ================================================

            OutlinedTextField(
                value = scoreText,

                onValueChange = { input ->

                    /*
                     * Allow digits and a decimal point.
                     *
                     * The final value is validated when
                     * the user clicks Add or Save.
                     */
                    if (input.all {
                            it.isDigit() || it == '.'
                        }) {

                        scoreText = input
                    }
                },

                label = {
                    Text("Score (%)")
                },

                placeholder = {
                    Text("Example: 92.5")
                },

                singleLine = true,

                modifier = Modifier.fillMaxWidth()
            )

            // ================================================
            // FORM ACTIONS
            // ================================================

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {


                Button(
                    enabled = students.isNotEmpty() &&
                            courses.isNotEmpty(),

                    onClick = {

                        // ================================================
                        // STEP 1: VALIDATE USER INPUT
                        // ================================================

                        val validationError = InputValidator.validateGrade(
                            studentId = selectedStudentId,
                            courseId = selectedCourseId,
                            scoreText = scoreText
                        )

                        if (validationError != null) {

                            // Invalid input: show error, do not save.
                            showMessage(validationError, true)

                        } else {

                            // ============================================
                            // STEP 2: SAVE VALID DATA
                            // ============================================

                            try {

                                /*
                                 * InputValidator has already checked
                                 * these values.
                                 */
                                val studentId =
                                    requireNotNull(selectedStudentId)

                                val courseId =
                                    requireNotNull(selectedCourseId)

                                val score =
                                    scoreText.toDouble()

                                /*
                                 * Build a Grade object using the
                                 * validated values.
                                 */
                                val grade = Grade(
                                    id = editingGradeId ?: 0L,
                                    studentId = studentId,
                                    courseId = courseId,
                                    score = score
                                )

                                // ========================================
                                // INSERT OR UPDATE
                                // ========================================

                                if (editingGradeId == null) {

                                    // New grade: SQL INSERT.
                                    gradeRepository.addGrade(grade)

                                    showMessage(
                                        "Grade added successfully."
                                    )

                                } else {

                                    // Existing grade: SQL UPDATE.
                                    gradeRepository.updateGrade(grade)

                                    showMessage(
                                        "Grade updated successfully."
                                    )
                                }

                                // ========================================
                                // REFRESH DATABASE RESULTS
                                // ========================================

                                /*
                                 * Refresh all grades, including
                                 * the SQL JOIN results.
                                 */
                                refreshData()

                                // Clear the form after successful save.
                                clearForm()

                            } catch (exception: Exception) {

                                // ========================================
                                // FRIENDLY DATABASE ERROR
                                // ========================================

                                showMessage(
                                    DatabaseErrorHandler.getMessage(exception),
                                    true
                                )
                            }
                        }
                    }
                ) {

                    Text(
                        if (editingGradeId == null)
                            "Add Grade"
                        else
                            "Save Changes"
                    )
                }





                if (editingGradeId != null) {

                    OutlinedButton(
                        onClick = {

                            clearForm()

                            showMessage("Edit cancelled.")
                        }
                    ) {

                        Text("Cancel")
                    }
                }
            }
        }

        // ====================================================
        // STATUS MESSAGE
        // ====================================================

        if (message.isNotBlank()) {

            Text(
                text = message,

                color = if (isError)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            )
        }

        HorizontalDivider()

        // ====================================================
        // GRADE LIST
        // ====================================================

        Text(
            text = "Grade Records",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Total grades: ${gradeDetails.size}"
        )

        if (students.isEmpty() || courses.isEmpty()) {

            Text(
                text = "Add at least one student and one course before recording grades."
            )

        } else if (gradeDetails.isEmpty()) {

            Text(
                text = "No grades have been recorded yet."
            )

        } else {

            // =================================================
            // SQL JOIN RESULTS
            // =================================================

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(
                    items = gradeDetails,
                    key = { it.gradeId }
                ) { details ->

                    GradeRow(
                        details = details,

                        // =====================================
                        // EDIT
                        // =====================================

                        onEdit = {

                            /*
                             * Find the original Grade object.
                             *
                             * GradeDetails contains readable
                             * names, while Grade contains
                             * the foreign key IDs needed
                             * for editing.
                             */
                            val grade = grades.firstOrNull {
                                it.id == details.gradeId
                            }

                            if (grade != null) {

                                editingGradeId = grade.id

                                selectedStudentId = grade.studentId

                                selectedCourseId = grade.courseId

                                scoreText = grade.score.toString()

                                showMessage(
                                    "Editing grade #${grade.id}"
                                )
                            }
                        },

                        // =====================================
                        // DELETE
                        // =====================================

                        onDelete = {

                            /*
                             * Do not delete immediately.
                             *
                             * Store the selected grade so the
                             * confirmation dialog can open.
                             */
                            gradePendingDelete = details
                        }


                    )
                }
            }
        }
    }


// ========================================================
// DELETE CONFIRMATION DIALOG
// ========================================================

    gradePendingDelete?.let { details ->

        ConfirmDeleteDialog(

            itemName =
                "${details.studentName}'s grade in ${details.courseCode}",

            onConfirm = {

                try {

                    // ========================================
                    // SQL DELETE
                    // ========================================

                    gradeRepository.deleteGrade(
                        details.gradeId
                    )

                    // ========================================
                    // REFRESH DATABASE RESULTS
                    // ========================================

                    refreshData()

                    /*
                     * If the deleted grade was being edited,
                     * clear the form.
                     */
                    if (editingGradeId == details.gradeId) {
                        clearForm()
                    }

                    showMessage(
                        "Grade deleted successfully."
                    )

                } catch (exception: Exception) {

                    showMessage(
                        DatabaseErrorHandler.getMessage(exception),
                        true
                    )

                } finally {

                    // Close the confirmation dialog.
                    gradePendingDelete = null
                }
            },

            onDismiss = {

                // Cancel without deleting anything.
                gradePendingDelete = null
            }
        )
    }





}

// ============================================================
// REUSABLE DROPDOWN COMPONENT
// ============================================================

/**
 * EntityDropdown
 *
 * A reusable dropdown for selecting either:
 *
 * - A student
 * - A course
 *
 * Each option is a Pair:
 *
 * Long   -> database ID
 * String -> readable label
 */
@Composable
private fun EntityDropdown(
    label: String,
    selectedText: String,
    options: List<Pair<Long, String>>,
    onSelected: (Long) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )

        Box {

            OutlinedButton(
                onClick = {
                    expanded = true
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text(selectedText)
            }

            DropdownMenu(
                expanded = expanded,

                onDismissRequest = {
                    expanded = false
                }
            ) {

                options.forEach { (id, text) ->

                    DropdownMenuItem(
                        text = {
                            Text(text)
                        },

                        onClick = {

                            onSelected(id)

                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// ============================================================
// GRADE ROW COMPONENT
// ============================================================

/**
 * GradeRow
 *
 * Displays one row from the SQL JOIN results.
 */
@Composable
private fun GradeRow(
    details: GradeDetails,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically,

            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.weight(1f),

                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = details.studentName,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${details.courseCode} — ${details.courseName}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Score: ${details.score}%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedButton(
                    onClick = onEdit
                ) {

                    Text("Edit")
                }

                Button(
                    onClick = onDelete
                ) {

                    Text("Delete")
                }
            }
        }
    }
}
