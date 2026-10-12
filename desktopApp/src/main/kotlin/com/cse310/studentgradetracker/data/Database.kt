package com.cse310.studentgradetracker.data
import java.sql.Connection
import java.sql.DriverManager


/**
 * Database
 *
 * This object is responsible for managing our application's
 * connection to the SQLite database.
 *
 * We use an `object` instead of a `class` because we only need
 * one database manager for the entire application.
 *
 * Kotlin's `object` keyword creates a singleton.
 */
object Database {


    /**
     * DATABASE_URL
     *
     * This tells JDBC which database we want to connect to.
     *
     * jdbc:sqlite:
     *     tells JDBC that we are using the SQLite driver.
     *
     * student_grades.db
     *     is the name of our SQLite database file.
     *
     * If this file does not already exist, SQLite will
     * automatically create it when we connect.
     */
    private const val DATABASE_URL =
        "jdbc:sqlite:student_grades.db"


    /**
     * connect()
     *
     * Creates and returns a connection to our SQLite database.
     *
     * Connection is a JDBC interface representing an active
     * connection between our Kotlin application and the database.
     */
    fun connect(): Connection {
        val connection =
            DriverManager.getConnection(DATABASE_URL)

        try {

            connection.createStatement().use { statement ->

                statement.execute(
                    "PRAGMA foreign_keys = ON;"
                )
            }

            return connection

        } catch (exception: Exception) {

            // Avoid leaking the connection if setup fails.
            connection.close()

            throw exception
        }

        /*
         * DriverManager is part of JDBC.
         *
         * getConnection() looks at our URL:
         *
         * jdbc:sqlite:student_grades.db
         *
         * and finds the SQLite JDBC driver that we added
         * through Gradle.
         */



    }



    /**
     * initialize()
     *
     * Sets up the database when our application starts.
     *
     * Currently its main responsibility is creating the
     * students table.
     *
     * Later we will expand this method to create:
     *
     * courses
     * assignments
     * grades
     */
    fun initialize() {

        /*
         * SQL command used to create our students table.
         *
         * Triple quotes """ allow Kotlin strings to span
         * multiple lines.
         */
        val createStudentsTable = """
            CREATE TABLE IF NOT EXISTS students (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE
            );
        """.trimIndent()


        /*
     * Stores courses.
     *
     * course_code is UNIQUE because we don't want two
     * separate courses with the same code.
     *
     * Example:
     *
     * CSE 310
     *
     * should appear only once.
     */

        val createCoursesTable = """
        CREATE TABLE IF NOT EXISTS courses (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            course_code TEXT NOT NULL UNIQUE,
            course_name TEXT NOT NULL,
            credits INTEGER NOT NULL
        );
    """.trimIndent()

        // ================================================
        // GRADES TABLE
        // ================================================

        /**
         * student_id references students.id.
         *
         * course_id references courses.id.
         *
         * CHECK prevents scores outside 0–100.
         *
         * ON DELETE RESTRICT prevents deleting a student
         * or course while grades still reference it.
         *
         * This helps protect database consistency.
         */
        val createGradesTable = """
            CREATE TABLE IF NOT EXISTS grades (
                id INTEGER PRIMARY KEY AUTOINCREMENT,

                student_id INTEGER NOT NULL,

                course_id INTEGER NOT NULL,

                score REAL NOT NULL
                    CHECK (score >= 0 AND score <= 100),

                FOREIGN KEY (student_id)
                    REFERENCES students(id)
                    ON DELETE RESTRICT,

                FOREIGN KEY (course_id)
                    REFERENCES courses(id)
                    ON DELETE RESTRICT,

                UNIQUE(student_id, course_id)
            );
        """.trimIndent()






        /*
         * connect().use { }
         *
         * First:
         *
         * connect()
         *
         * opens the database connection.
         *
         * Kotlin's use {} function automatically closes the
         * connection when the block finishes.
         *
         * This is important because database connections are
         * resources that should not remain open unnecessarily.
         */
        connect().use { connection ->


            /*
             * createStatement()
             *
             * Creates a JDBC Statement object.
             *
             * A Statement lets us send SQL commands from
             * Kotlin to SQLite.
             */
            connection.createStatement().use { statement ->
                /*
                 * execute()
                 *
                 * Sends our CREATE TABLE SQL command to SQLite.
                 */
                statement.execute(createStudentsTable)
            }

            // =====================================================
            // CREATE COURSES TABLE
            // =====================================================

            connection.createStatement().use { statement ->

                statement.execute(createCoursesTable)
            }


            connection.createStatement().use { statement ->

                statement.execute(createGradesTable)
            }





        }


        /*
         * This message is mainly for us during development.
         *
         * Look for it in IntelliJ's Run console.
         */
        println("Database initialized successfully.")
    }
}