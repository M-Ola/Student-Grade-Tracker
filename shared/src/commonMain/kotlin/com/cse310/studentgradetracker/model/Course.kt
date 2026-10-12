package com.cse310.studentgradetracker.model




/**
 * Course
 *
 * Represents ONE course in the Student Grade Tracker.
 *
 * Just like Student represents a row from the students table,
 * Course represents a row from the courses table.
 *
 * Example database row:
 *
 * ---------------------------------------------------
 * id | course_code | course_name              | credits
 * ---------------------------------------------------
 * 1  | CSE 310     | Applied Programming      | 3
 * ---------------------------------------------------
 *
 * becomes:
 *
 * Course(
 *     id = 1,
 *     courseCode = "CSE 310",
 *     courseName = "Applied Programming",
 *     credits = 3
 * )
 */
data class Course(

    /**
     * SQLite primary key.
     *
     * New Course objects use 0 until SQLite generates
     * the actual ID.
     */
    val id: Long = 0,


    /**
     * Short identifier for the course.
     *
     * Examples:
     *
     * CSE 310
     * CSE 325
     * MATH 110
     */
    val courseCode: String,


    /**
     * Human-readable course name.
     */
    val courseName: String,


    /**
     * Number of course credits.
     *
     * This also gives us another numerical value that
     * could later be summarized using SQL.
     */
    val credits: Int
)