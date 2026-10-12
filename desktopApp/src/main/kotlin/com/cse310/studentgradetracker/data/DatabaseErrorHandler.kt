package com.cse310.studentgradetracker.data


import java.sql.SQLException
import java.util.Locale

/**
 * DatabaseErrorHandler
 *
 * Converts common SQLite errors into readable messages.
 *
 * The original exception is still available to the
 * developer for logging and debugging.
 */
object DatabaseErrorHandler {

    fun getMessage(exception: Exception): String {

        /*
         * SQLException covers JDBC database failures.
         */
        if (exception is SQLException) {

            val details = (
                    exception.message ?: ""
                    ).lowercase(Locale.ROOT)

            // Duplicate email or course code.
            if (
                "unique constraint" in details ||
                "constraint_unique" in details
            ) {
                return "This record already exists. Check for duplicate email addresses, course codes, or grades."
            }

            // Invalid student/course relationship.
            if (
                "foreign key constraint" in details ||
                "constraint_foreignkey" in details
            ) {
                return "This operation conflicts with an existing student, course, or grade relationship."
            }

            // Invalid database value.
            if (
                "check constraint" in details ||
                "constraint_check" in details
            ) {
                return "One or more values are outside the allowed range."
            }

            // SQLite database is locked.
            if ("database is locked" in details ||
                "busy" in details) {
                return "The database is busy. Please try again."
            }

            return "The database operation could not be completed."
        }

        /*
         * Validation or other non-SQL errors.
         */
        return exception.message
            ?: "An unexpected error occurred."
    }
}
