package com.cse310.studentgradetracker
import com.cse310.studentgradetracker.data.Database
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cse310.studentgradetracker.ui.StudentsScreen

/*
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "StudentGradeTracker",
    ) {
        App()
    }
}*/


/**
 * main()
 *
 * This is the entry point of our Student Grade Tracker.
 *
 * The operating system starts our Kotlin application here.
 */
fun main() {


    /*
     * STEP 1
     * ------
     *
     * Prepare our database.
     *
     * initialize() connects to SQLite and creates the
     * students table if it does not already exist.
     *
     * We deliberately do this BEFORE starting the UI.
     */
    Database.initialize()


    /*
     * STEP 2
     * ------
     *
     * Start the Compose Desktop application.
     */
    application {


        /*
         * STEP 3
         * ------
         *
         * Create the application's desktop window.
         */
        Window(

            /*
             * When the user presses the window's close button,
             * exit the Compose application.
             */
            onCloseRequest = ::exitApplication,


            /*
             * Text displayed in the window's title bar.
             */
            title = "StudentGradeTracker",

            ) {


            /*
             * STEP 4
             * ------
             *
             * Display our Compose UI.
             */
            DesktopApp()
        }
    }
}
