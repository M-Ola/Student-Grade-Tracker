package com.cse310.studentgradetracker.data

import com.cse310.studentgradetracker.model.Grade
import com.cse310.studentgradetracker.model.GradeDetails


/**
 * GradeRepository
 *
 * Performs CRUD operations on the grades table.
 *
 * All SQL is executed through JDBC.
 *
 * This keeps database logic separate from Compose UI.
 */
class GradeRepository {

    // =====================================================
    // CREATE — INSERT
    // =====================================================

    /**
     * Inserts a grade into SQLite.
     *
     * The student and course must already exist.
     */
    fun addGrade(grade: Grade) {

        require(grade.score in 0.0..100.0) {
            "Score must be between 0 and 100."
        }

        val sql = """
            INSERT INTO grades (
                student_id,
                course_id,
                score
            )
            VALUES (?, ?, ?);
        """.trimIndent()

        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->

                // First parameter: student ID.
                statement.setLong(1, grade.studentId)

                // Second parameter: course ID.
                statement.setLong(2, grade.courseId)

                // Third parameter: numerical score.
                statement.setDouble(3, grade.score)

                // Submit INSERT to SQLite.
                statement.executeUpdate()
            }
        }
    }

    // =====================================================
    // READ — SELECT
    // =====================================================

    /**
     * Retrieves all grades from SQLite.
     *
     * Each ResultSet row becomes a Kotlin Grade object.
     */
    fun getAllGrades(): List<Grade> {

        val grades = mutableListOf<Grade>()

        val sql = """
            SELECT
                id,
                student_id,
                course_id,
                score
            FROM grades
            ORDER BY id;
        """.trimIndent()

        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->

                statement.executeQuery().use { resultSet ->

                    // Process each row returned by SQLite.
                    while (resultSet.next()) {

                        val grade = Grade(
                            id = resultSet.getLong("id"),
                            studentId = resultSet.getLong("student_id"),
                            courseId = resultSet.getLong("course_id"),
                            score = resultSet.getDouble("score")
                        )

                        grades.add(grade)
                    }
                }
            }
        }

        return grades
    }

    // =====================================================
    // UPDATE — UPDATE
    // =====================================================

    /**
     * Modifies an existing grade.
     *
     * The WHERE clause ensures only the grade
     * with the specified ID is updated.
     */
    fun updateGrade(grade: Grade) {

        require(grade.score in 0.0..100.0) {
            "Score must be between 0 and 100."
        }

        val sql = """
            UPDATE grades
            SET
                student_id = ?,
                course_id = ?,
                score = ?
            WHERE id = ?;
        """.trimIndent()

        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->

                statement.setLong(1, grade.studentId)

                statement.setLong(2, grade.courseId)

                statement.setDouble(3, grade.score)

                // Identify the existing grade.
                statement.setLong(4, grade.id)

                statement.executeUpdate()
            }
        }
    }

    // =====================================================
    // DELETE — DELETE
    // =====================================================

    /**
     * Deletes a grade by its primary key.
     */
    fun deleteGrade(gradeId: Long) {

        val sql = """
            DELETE FROM grades
            WHERE id = ?;
        """.trimIndent()

        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->

                statement.setLong(1, gradeId)

                statement.executeUpdate()
            }
        }
    }



    /**
     * getGradeDetails()
     *
     * Retrieves grades together with student and course
     * information using SQL JOIN.
     *
     * This demonstrates the additional SQL requirement:
     *
     * "Perform a join in your software between two tables."
     *
     * We actually join THREE tables.
     */
    fun getGradeDetails(): List<GradeDetails> {

        // Holds the results returned by SQLite.
        val gradeDetails = mutableListOf<GradeDetails>()

        /**
         * SQL JOIN
         *
         * grades is our starting table.
         *
         * First JOIN:
         * Match grades.student_id with students.id.
         *
         * Second JOIN:
         * Match grades.course_id with courses.id.
         *
         * AS student_name creates a convenient alias
         * for the combined first and last name.
         */
        val sql = """
        SELECT
            grades.id AS grade_id,

            students.first_name || ' ' ||
            students.last_name AS student_name,

            courses.course_code,

            courses.course_name,

            grades.score

        FROM grades

        INNER JOIN students
            ON grades.student_id = students.id

        INNER JOIN courses
            ON grades.course_id = courses.id

        ORDER BY
            students.last_name,
            students.first_name,
            courses.course_code;
    """.trimIndent()

        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->

                /**
                 * executeQuery() submits the JOIN to SQLite.
                 *
                 * SQLite returns a ResultSet containing
                 * combined information from all three tables.
                 */
                statement.executeQuery().use { resultSet ->

                    while (resultSet.next()) {

                        /**
                         * Convert each returned SQL row into
                         * a Kotlin GradeDetails object.
                         */
                        val details = GradeDetails(

                            gradeId =
                                resultSet.getLong("grade_id"),

                            studentName =
                                resultSet.getString("student_name"),

                            courseCode =
                                resultSet.getString("course_code"),

                            courseName =
                                resultSet.getString("course_name"),

                            score =
                                resultSet.getDouble("score")
                        )

                        // Store the result for our application.
                        gradeDetails.add(details)
                    }
                }
            }
        }

        // Return readable results to the caller.
        return gradeDetails
    }



}
