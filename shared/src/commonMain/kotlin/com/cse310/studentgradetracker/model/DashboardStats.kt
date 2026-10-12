package com.cse310.studentgradetracker.model




/**
 * DashboardStats
 *
 * Represents summary statistics calculated
 * directly by SQLite.
 *
 * These values are not stored in a separate table.
 * They are calculated using SQL aggregate functions.
 */
data class DashboardStats(

    // Number of records in students table.
    val totalStudents: Int,

    // Number of records in courses table.
    val totalCourses: Int,

    // Number of records in grades table.
    val totalGrades: Int,

    /**
     * Average score of all recorded grades.
     *
     * Nullable because AVG() returns NULL
     * when there are no grades.
     */
    val averageGrade: Double?
)
