package com.cse310.studentgradetracker.validation




/**
 * InputValidator
 *
 * Centralizes input validation for:
 *
 * - Students
 * - Courses
 * - Grades
 *
 * The functions return:
 *
 * null   -> Input is valid.
 * String -> Explanation of the validation error.
 *
 * This class contains no database or UI dependencies,
 * so it can safely live in commonMain.
 */
object InputValidator {

    // =====================================================
    // STUDENT VALIDATION
    // =====================================================

    fun validateStudent(
        firstName: String,
        lastName: String,
        email: String
    ): String? {

        if (firstName.isBlank()) {
            return "First name is required."
        }

        if (lastName.isBlank()) {
            return "Last name is required."
        }

        if (firstName.trim().length > 100 ||
            lastName.trim().length > 100) {
            return "Names must be 100 characters or fewer."
        }

        if (email.isBlank()) {
            return "Email address is required."
        }

        /*
         * A basic email format check.
         *
         * This is suitable for form validation,
         * but does not prove the email exists.
         */
        val emailPattern =
            Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")

        if (!emailPattern.matches(email.trim())) {
            return "Enter a valid email address."
        }

        if (email.trim().length > 254) {
            return "Email address is too long."
        }

        return null
    }

    // =====================================================
    // COURSE VALIDATION
    // =====================================================

    fun validateCourse(
        courseCode: String,
        courseName: String,
        creditsText: String
    ): String? {

        if (courseCode.isBlank()) {
            return "Course code is required."
        }

        if (courseCode.trim().length > 20) {
            return "Course code must be 20 characters or fewer."
        }

        if (courseName.isBlank()) {
            return "Course name is required."
        }

        if (courseName.trim().length > 150) {
            return "Course name must be 150 characters or fewer."
        }

        val credits = creditsText.toIntOrNull()
            ?: return "Credits must be a whole number."

        if (credits !in 1..30) {
            return "Credits must be between 1 and 30."
        }

        return null
    }

    // =====================================================
    // GRADE VALIDATION
    // =====================================================

    fun validateGrade(
        studentId: Long?,
        courseId: Long?,
        scoreText: String
    ): String? {

        if (studentId == null || studentId <= 0) {
            return "Please select a student."
        }

        if (courseId == null || courseId <= 0) {
            return "Please select a course."
        }

        val score = scoreText.toDoubleOrNull()
            ?: return "Enter a valid numerical score."

        /*
         * isFinite() rejects Infinity and NaN.
         */
        if (!score.isFinite() || score !in 0.0..100.0) {
            return "Score must be between 0 and 100."
        }

        return null
    }
}
