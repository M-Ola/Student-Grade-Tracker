package com.cse310.studentgradetracker.ui



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
import com.cse310.studentgradetracker.validation.InputValidator
import com.cse310.studentgradetracker.data.DatabaseErrorHandler


// ============================================================
// APPLICATION IMPORTS
// ============================================================

import com.cse310.studentgradetracker.data.CourseRepository
import com.cse310.studentgradetracker.model.Course


/**
 * CoursesScreen
 *
 * Allows the user to manage courses.
 *
 * The screen demonstrates:
 *
 * INSERT
 * SELECT
 * UPDATE
 * DELETE
 *
 * against the SQLite courses table.
 */
@Composable
fun CoursesScreen() {


    // =========================================================
    // REPOSITORY
    // =========================================================

    /*
     * Keep one CourseRepository while this composable
     * remains in the composition.
     */
    val repository = remember {

        CourseRepository()
    }


    // =========================================================
    // FORM STATE
    // =========================================================

    /*
     * Course code entered by the user.
     *
     * Example:
     *
     * CSE 310
     */
    var courseCode by remember {

        mutableStateOf("")
    }


    var coursePendingDelete by remember {
        mutableStateOf<Course?>(null)
    }


    /*
     * Full course name.
     */
    var courseName by remember {

        mutableStateOf("")
    }


    /*
     * Keep credits as text while the user is typing.
     *
     * Later we convert the String into an Int before
     * sending it to SQLite.
     */
    var creditsText by remember {

        mutableStateOf("")
    }


    // =========================================================
    // EDIT STATE
    // =========================================================

    /*
     * null:
     *
     *     We are adding a new course.
     *
     * Long value:
     *
     *     We are editing an existing course.
     */
    var editingCourseId by remember {

        mutableStateOf<Long?>(null)
    }


    // =========================================================
    // DATABASE RESULTS
    // =========================================================

    /*
     * Query SQLite when this screen is initially created.
     */
    var courses by remember {

        mutableStateOf<List<Course>>(
            repository.getAllCourses()
        )
    }


    // =========================================================
    // STATUS MESSAGE
    // =========================================================

    var message by remember {

        mutableStateOf("")
    }


    // =========================================================
    // HELPER FUNCTIONS
    // =========================================================

    /**
     * Run another SELECT query and update Compose state.
     */
    fun refreshCourses() {

        courses =
            repository.getAllCourses()
    }


    /**
     * Reset the course form.
     */
    fun clearForm() {

        courseCode = ""

        courseName = ""

        creditsText = ""

        editingCourseId = null
    }


    // =========================================================
    // SCREEN
    // =========================================================

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)

    ) {


        // =====================================================
        // HEADER
        // =====================================================

        Text(
            text = "Courses",
            style = MaterialTheme.typography.headlineLarge
        )


        Text(
            text = "Add, edit, and manage courses.",
            style = MaterialTheme.typography.bodyMedium
        )


        HorizontalDivider()


        // =====================================================
        // FORM TITLE
        // =====================================================

        Text(

            text =
                if (editingCourseId == null)
                    "Add Course"
                else
                    "Edit Course",

            style =
                MaterialTheme.typography.headlineSmall
        )


        // =====================================================
        // COURSE CODE
        // =====================================================

        OutlinedTextField(

            value = courseCode,

            onValueChange = {

                courseCode = it
            },

            label = {

                Text("Course Code")
            },

            placeholder = {

                Text("Example: CSE 310")
            },

            singleLine = true,

            modifier =
                Modifier.fillMaxWidth()
        )


        // =====================================================
        // COURSE NAME
        // =====================================================

        OutlinedTextField(

            value = courseName,

            onValueChange = {

                courseName = it
            },

            label = {

                Text("Course Name")
            },

            placeholder = {

                Text("Example: Applied Programming")
            },

            singleLine = true,

            modifier =
                Modifier.fillMaxWidth()
        )


        // =====================================================
        // CREDITS
        // =====================================================

        OutlinedTextField(

            value = creditsText,

            onValueChange = {

                /*
                 * Only allow digits into this field.
                 *
                 * Empty text is allowed so the user can
                 * erase and replace the value.
                 */
                if (
                    it.isEmpty() ||
                    it.all { character ->
                        character.isDigit()
                    }
                ) {

                    creditsText = it
                }
            },

            label = {

                Text("Credits")
            },

            placeholder = {

                Text("Example: 3")
            },

            singleLine = true,

            modifier =
                Modifier.fillMaxWidth()
        )


        // =====================================================
        // FORM BUTTONS
        // =====================================================

        Row(

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)

        ) {


            Button(

                onClick = {




                    val validationError = InputValidator.validateCourse(
                        courseCode = courseCode,
                        courseName = courseName,
                        creditsText = creditsText
                    )

                    if (validationError != null) {

                        message = validationError

                    } else {

                        try {

                            // Validation guarantees this is a valid Int.
                            val credits = creditsText.toInt()

                            val course = Course(
                                id = editingCourseId ?: 0L,
                                courseCode = courseCode.trim().uppercase(),
                                courseName = courseName.trim(),
                                credits = credits
                            )

                            if (editingCourseId == null) {

                                // INSERT
                                repository.addCourse(course)

                                message = "Course added successfully."

                            } else {

                                // UPDATE
                                repository.updateCourse(course)

                                message = "Course updated successfully."
                            }

                            refreshCourses()
                            clearForm()

                        } catch (exception: Exception) {

                            message =
                                DatabaseErrorHandler.getMessage(exception)
                        }
                    }




                }



            ) {

                Text(

                    if (editingCourseId == null)
                        "Add Course"
                    else
                        "Save Changes"
                )
            }


            // =================================================
            // CANCEL EDIT
            // =================================================

            if (editingCourseId != null) {

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


        // =====================================================
        // STATUS MESSAGE
        // =====================================================

        if (message.isNotBlank()) {

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }


        HorizontalDivider()


        // =====================================================
        // COURSE LIST HEADER
        // =====================================================

        Text(
            text = "Course List",
            style = MaterialTheme.typography.headlineSmall
        )


        Text(
            text = "Total courses: ${courses.size}",
            style = MaterialTheme.typography.bodyMedium
        )


        // =====================================================
        // EMPTY STATE
        // =====================================================

        if (courses.isEmpty()) {

            Text(
                text = "No courses have been added yet."
            )

        } else {


            // =================================================
            // DATABASE RESULTS
            // =================================================

            LazyColumn(

                verticalArrangement =
                    Arrangement.spacedBy(8.dp),

                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)

            ) {


                items(

                    items = courses,

                    key = { course ->
                        course.id
                    }

                ) { course ->


                    CourseRow(

                        course = course,


                        // =====================================
                        // EDIT
                        // =====================================

                        onEdit = {


                            /*
                             * Remember which database row
                             * we're editing.
                             */
                            editingCourseId =
                                course.id


                            /*
                             * Populate the form with existing
                             * database values.
                             */
                            courseCode =
                                course.courseCode

                            courseName =
                                course.courseName

                            creditsText =
                                course.credits.toString()


                            message =
                                "Editing ${course.courseCode}"
                        },


                        // =====================================
                        // DELETE
                        // =====================================

                        onDelete = {


                            coursePendingDelete = course


                        }



                    )
                }
            }
        }
    }


    coursePendingDelete?.let { course ->

        ConfirmDeleteDialog(

            itemName = "${course.courseCode} — ${course.courseName}",

            onConfirm = {

                try {

                    // DELETE FROM courses WHERE id = ?
                    repository.deleteCourse(course.id)

                    refreshCourses()

                    if (editingCourseId == course.id) {
                        clearForm()
                    }

                    message = "Course deleted successfully."

                } catch (exception: Exception) {

                    message =
                        DatabaseErrorHandler.getMessage(exception)

                } finally {

                    coursePendingDelete = null
                }
            },

            onDismiss = {
                coursePendingDelete = null
            }
        )
    }




}


/**
 * CourseRow
 *
 * Displays ONE Course retrieved from SQLite.
 */
@Composable
private fun CourseRow(

    course: Course,

    onEdit: () -> Unit,

    onDelete: () -> Unit

) {


    Card(

        modifier =
            Modifier.fillMaxWidth()

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


            // =================================================
            // COURSE INFORMATION
            // =================================================

            Column(

                modifier =
                    Modifier.weight(1f)

            ) {


                Text(

                    text =
                        course.courseCode,

                    style =
                        MaterialTheme.typography.titleMedium
                )


                Text(

                    text =
                        course.courseName,

                    style =
                        MaterialTheme.typography.bodyMedium
                )


                Text(

                    text =
                        "${course.credits} credits",

                    style =
                        MaterialTheme.typography.bodySmall
                )


                /*
                 * Showing the primary key is useful while
                 * we're developing and demonstrating SQL.
                 */
                Text(

                    text =
                        "Course ID: ${course.id}",

                    style =
                        MaterialTheme.typography.bodySmall
                )
            }


            // =================================================
            // ACTIONS
            // =================================================

            Row(

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)

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