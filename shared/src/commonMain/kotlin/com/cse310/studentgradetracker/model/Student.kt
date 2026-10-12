package com.cse310.studentgradetracker.model


/**
 * Student
 *
 * This data class represents ONE student inside our application.
 *
 * The Student object is the Kotlin representation of a row
 * from the SQLite "students" table.
 *
 * For example, this database row:
 *
 * ----------------------------------------------------------
 * id | first_name | last_name | email
 * ----------------------------------------------------------
 * 1  | Alice      | Johnson   | alice@school.edu
 * ----------------------------------------------------------
 *
 * becomes this Kotlin object:
 *
 * Student(
 *     id = 1,
 *     firstName = "Alice",
 *     lastName = "Johnson",
 *     email = "alice@school.edu"
 * )
 */
data class Student(

    /**
     * Unique ID for the student.
     *
     * SQLite automatically generates this value because
     * our database column uses:
     *
     * INTEGER PRIMARY KEY AUTOINCREMENT
     *
     * We use Long because SQLite integer values can be
     * larger than Kotlin's Int range.
     *
     * A new student starts with id = 0 because the database
     * has not assigned an ID yet.
     */
    val id: Long = 0,


    /**
     * Student's first name.
     *
     * This corresponds to:
     *
     * first_name TEXT NOT NULL
     *
     * in our SQLite table.
     */
    val firstName: String,


    /**
     * Student's last name.
     *
     * This corresponds to:
     *
     * last_name TEXT NOT NULL
     */
    val lastName: String,


    /**
     * Student's email address.
     *
     * The database requires this value to be unique.
     *
     * This corresponds to:
     *
     * email TEXT NOT NULL UNIQUE
     */
    val email: String
)