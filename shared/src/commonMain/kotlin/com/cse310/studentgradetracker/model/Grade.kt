package com.cse310.studentgradetracker.model


/**
 * Grade
 *
 * Represents one grade record stored in SQLite.
 *
 * Unlike Student and Course, this model contains
 * foreign keys that reference two other tables.
 *
 * Example:
 *
 * Grade(
 *     id = 1,
 *     studentId = 2,
 *     courseId = 3,
 *     score = 92.0
 * )
 *
 * Meaning:
 * Student #2 received a score of 92 in Course #3.
 */
data class Grade(

    /**
     * Primary key generated automatically by SQLite.
     *
     * A new grade uses 0 until it is inserted.
     */
    val id: Long = 0,

    /**
     * Foreign key referencing students.id.
     *
     * This tells SQLite which student owns the grade.
     */
    val studentId: Long,

    /**
     * Foreign key referencing courses.id.
     *
     * This identifies the course for the grade.
     */
    val courseId: Long,

    /**
     * Student's percentage score.
     *
     * Double allows values such as 89.5.
     *
     * We will restrict scores to 0–100.
     */
    val score: Double
)
