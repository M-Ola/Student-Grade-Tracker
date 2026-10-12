package com.cse310.studentgradetracker


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.cse310.studentgradetracker.ui.StudentsScreen
import com.cse310.studentgradetracker.ui.CoursesScreen
import com.cse310.studentgradetracker.ui.GradesScreen
import com.cse310.studentgradetracker.ui.DashboardScreen

/**
 * AppScreen
 *
 * Defines the available screens.
 *
 * Each enum value represents one application section.
 */
enum class AppScreen {

    STUDENTS,

    COURSES,

    GRADES,

    DASHBOARD


}

/**
 * DesktopApp
 *
 * Main application container.
 *
 * Controls navigation between Students, Courses,
 * and Grades.
 */
@Composable
fun DesktopApp() {

    /*
     * Remember the currently selected screen.
     *
     * The application starts on Students.
     */
    var currentScreen by remember {
        mutableStateOf(AppScreen.STUDENTS)
    }

    MaterialTheme {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // ================================================
            // NAVIGATION BAR
            // ================================================

            Surface(
                tonalElevation = 2.dp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    // STUDENTS BUTTON

                    NavigationButton(
                        text = "Students",
                        selected = currentScreen == AppScreen.STUDENTS,
                        onClick = {
                            currentScreen = AppScreen.STUDENTS
                        }
                    )

                    // COURSES BUTTON

                    NavigationButton(
                        text = "Courses",
                        selected = currentScreen == AppScreen.COURSES,
                        onClick = {
                            currentScreen = AppScreen.COURSES
                        }
                    )

                    // GRADES BUTTON

                    NavigationButton(
                        text = "Grades",
                        selected = currentScreen == AppScreen.GRADES,
                        onClick = {
                            currentScreen = AppScreen.GRADES
                        }
                    )

// ====================================================
// DASHBOARD NAVIGATION
// ====================================================

                    NavigationButton(
                        text = "Dashboard",

                        selected = currentScreen == AppScreen.DASHBOARD,

                        onClick = {
                            currentScreen = AppScreen.DASHBOARD
                        }
                    )

                }




            }

            // ================================================
            // SCREEN CONTENT
            // ================================================

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                when (currentScreen) {

                    AppScreen.STUDENTS -> {
                        StudentsScreen()
                    }

                    AppScreen.COURSES -> {
                        CoursesScreen()
                    }

                    AppScreen.GRADES -> {
                        GradesScreen()
                    }

                    AppScreen.DASHBOARD -> {
                        DashboardScreen()
                    }


                }
            }
        }
    }
}

/**
 * NavigationButton
 *
 * Displays either a filled or outlined button,
 * depending on whether its screen is selected.
 */
@Composable
private fun NavigationButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    if (selected) {

        Button(
            onClick = onClick
        ) {

            Text(text)
        }

    } else {

        OutlinedButton(
            onClick = onClick
        ) {

            Text(text)
        }
    }
}
