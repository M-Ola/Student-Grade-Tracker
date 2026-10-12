package com.cse310.studentgradetracker.data

import com.cse310.studentgradetracker.model.DashboardStats
import com.cse310.studentgradetracker.model.CourseAverage

/**
 * DashboardRepository
 *
 * Executes SQL queries used by the dashboard.
 *
 * Demonstrates:
 *
 * COUNT()
 * AVG()
 * GROUP BY
 * INNER JOIN
 *
 * All statistics are calculated by SQLite.
 */
class DashboardRepository {

    // ========================================================
    // DASHBOARD SUMMARY
    // ========================================================

    /**
     * getDashboardStats()
     *
     * Retrieves four aggregate values:
     *
     * 1. Total students.
     * 2. Total courses.
     * 3. Total grade records.
     * 4. Average grade.
     *
     * SQL COUNT() counts matching rows.
     *
     * SQL AVG() calculates the arithmetic mean.
     */
    fun getDashboardStats(): DashboardStats {

        /**
         * Each subquery returns one aggregate value.
         *
         * Using one SQL statement lets us retrieve
         * all four values in a single ResultSet row.
         */
        val sql = """
            SELECT
                (SELECT COUNT(*) FROM students)
                    AS total_students,

                (SELECT COUNT(*) FROM courses)
                    AS total_courses,

                (SELECT COUNT(*) FROM grades)
                    AS total_grades,

                (SELECT AVG(score) FROM grades)
                    AS average_grade;
        """.trimIndent()

        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->

                statement.executeQuery().use { resultSet ->

                    /*
                     * The aggregate SELECT should return
                     * exactly one row.
                     */
                    check(resultSet.next()) {
                        "Dashboard query returned no results."
                    }

                    /**
                     * SQLite returns COUNT() as an integer.
                     */
                    val totalStudents =
                        resultSet.getInt("total_students")

                    val totalCourses =
                        resultSet.getInt("total_courses")

                    val totalGrades =
                        resultSet.getInt("total_grades")

                    /**
                     * AVG() returns NULL when the grades
                     * table is empty.
                     *
                     * getDouble() returns 0.0 for SQL NULL,
                     * so we must check wasNull().
                     */
                    val averageValue =
                        resultSet.getDouble("average_grade")

                    val averageGrade =
                        if (resultSet.wasNull()) {
                            null
                        } else {
                            averageValue
                        }

                    /*
                     * Convert SQL results into a Kotlin
                     * data object.
                     */
                    return DashboardStats(
                        totalStudents = totalStudents,
                        totalCourses = totalCourses,
                        totalGrades = totalGrades,
                        averageGrade = averageGrade
                    )
                }
            }
        }
    }

    // ========================================================
    // AVERAGE GRADE BY COURSE
    // ========================================================

    /**
     * getCourseAverages()
     *
     * Retrieves the average score for every course
     * that currently has recorded grades.
     *
     * Demonstrates:
     *
     * INNER JOIN
     * GROUP BY
     * COUNT()
     * AVG()
     */
    fun getCourseAverages(): List<CourseAverage> {

        val results = mutableListOf<CourseAverage>()

        val sql = """
            SELECT
                courses.course_code,

                courses.course_name,

                COUNT(grades.id) AS total_grades,

                AVG(grades.score) AS average_score

            FROM courses

            INNER JOIN grades
                ON courses.id = grades.course_id

            GROUP BY
                courses.id,
                courses.course_code,
                courses.course_name

            ORDER BY courses.course_code;
        """.trimIndent()

        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->

                statement.executeQuery().use { resultSet ->

                    /*
                     * Each returned row represents
                     * one course and its aggregate results.
                     */
                    while (resultSet.next()) {

                        val courseAverage = CourseAverage(

                            courseCode =
                                resultSet.getString("course_code"),

                            courseName =
                                resultSet.getString("course_name"),

                            totalGrades =
                                resultSet.getInt("total_grades"),

                            averageScore =
                                resultSet.getDouble("average_score")
                        )

                        results.add(courseAverage)
                    }
                }
            }
        }

        return results
    }
}
