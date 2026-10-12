package com.cse310.studentgradetracker.model



/**
 * GradeDetails
 *
 * Represents the result of joining:
 *
 * grades
 * students
 * courses
 *
 * Unlike Grade, this model contains readable names
 * instead of only foreign key IDs.
 *
 * This is a query-result model, not a separate SQL table.
 */
data class GradeDetails(

    // Unique grade record.
    val gradeId: Long,

    // Student's full name.
    val studentName: String,

    // Course identifier, such as CSE 310.
    val courseCode: String,

    // Full course name.
    val courseName: String,

    // Numerical grade percentage.
    val score: Double
)
