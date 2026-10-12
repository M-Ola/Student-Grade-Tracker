package com.cse310.studentgradetracker.model


/**
 * CourseAverage
 *
 * Represents aggregate grade information
 * for one course.
 *
 * Example:
 *
 * CourseAverage(
 *     courseCode = "CSE 310",
 *     courseName = "Applied Programming",
 *     totalGrades = 5,
 *     averageScore = 88.4
 * )
 *
 * This model is populated from SQL GROUP BY results.
 */
data class CourseAverage(

    // Example: CSE 310
    val courseCode: String,

    // Example: Applied Programming
    val courseName: String,

    // Number of grade records for this course.
    val totalGrades: Int,

    // Average score for this course.
    val averageScore: Double
)
