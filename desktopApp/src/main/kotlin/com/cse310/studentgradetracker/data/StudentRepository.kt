package com.cse310.studentgradetracker.data

import com.cse310.studentgradetracker.model.Student


/**
 * StudentRepository
 *
 * This class contains all database operations related
 * to students.
 *
 * Instead of writing SQL directly inside our Compose UI,
 * we keep SQL here.
 *
 * This gives us a cleaner architecture:
 *
 * UI
 * ↓
 * Repository
 * ↓
 * Database
 */
class StudentRepository {


    // =========================================================
    // CREATE
    // =========================================================

    /**
     * addStudent()
     *
     * Adds a new student to the SQLite database.
     *
     * SQL operation:
     *
     * INSERT INTO students ...
     */
    fun addStudent(student: Student) {

        /*
         * The question marks are SQL parameters.
         *
         * We do NOT directly put user-entered values
         * inside the SQL string.
         *
         * Instead:
         *
         * ? #1 = first name
         * ? #2 = last name
         * ? #3 = email
         *
         * We will provide those values using a
         * PreparedStatement.
         */
        val sql = """
            INSERT INTO students (
                first_name,
                last_name,
                email
            )
            VALUES (?, ?, ?);
        """.trimIndent()


        /*
         * Open a connection to SQLite.
         *
         * use {} automatically closes the connection
         * after this block finishes.
         */
        Database.connect().use { connection ->


            /*
             * prepareStatement() creates a PreparedStatement.
             *
             * Prepared statements are preferable to building
             * SQL by concatenating strings.
             */
            connection.prepareStatement(sql).use { statement ->


                /*
                 * Replace the FIRST ? with the student's
                 * first name.
                 */
                statement.setString(
                    1,
                    student.firstName
                )


                /*
                 * Replace the SECOND ? with the student's
                 * last name.
                 */
                statement.setString(
                    2,
                    student.lastName
                )


                /*
                 * Replace the THIRD ? with the student's
                 * email.
                 */
                statement.setString(
                    3,
                    student.email
                )


                /*
                 * executeUpdate() runs SQL statements that
                 * change data.
                 *
                 * Examples:
                 *
                 * INSERT
                 * UPDATE
                 * DELETE
                 */
                statement.executeUpdate()
            }
        }
    }


    // =========================================================
    // READ
    // =========================================================

    /**
     * getAllStudents()
     *
     * Retrieves every student stored in the database.
     *
     * SQL operation:
     *
     * SELECT
     */
    fun getAllStudents(): List<Student> {

        /*
         * This list will hold the Student objects
         * we retrieve from SQLite.
         */
        val students = mutableListOf<Student>()


        /*
         * SQL query.
         *
         * We explicitly name the columns we need.
         */
        val sql = """
            SELECT
                id,
                first_name,
                last_name,
                email
            FROM students
            ORDER BY last_name, first_name;
        """.trimIndent()


        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->


                /*
                 * executeQuery() is used for SELECT statements.
                 *
                 * It returns a ResultSet.
                 *
                 * Think of a ResultSet as the rows returned
                 * from the database.
                 */
                statement.executeQuery().use { resultSet ->


                    /*
                     * resultSet.next()
                     *
                     * Moves to the next database row.
                     *
                     * It returns true while another row exists.
                     */
                    while (resultSet.next()) {


                        /*
                         * Convert the current database row
                         * into a Kotlin Student object.
                         */
                        val student = Student(

                            id = resultSet.getLong("id"),

                            firstName =
                                resultSet.getString("first_name"),

                            lastName =
                                resultSet.getString("last_name"),

                            email =
                                resultSet.getString("email")
                        )


                        /*
                         * Add the Student object to our
                         * Kotlin list.
                         */
                        students.add(student)
                    }
                }
            }
        }


        /*
         * Return all students to whoever called
         * getAllStudents().
         */
        return students
    }


    // =========================================================
    // UPDATE
    // =========================================================

    /**
     * updateStudent()
     *
     * Updates an existing student.
     *
     * SQL operation:
     *
     * UPDATE
     */
    fun updateStudent(student: Student) {

        val sql = """
            UPDATE students
            SET
                first_name = ?,
                last_name = ?,
                email = ?
            WHERE id = ?;
        """.trimIndent()


        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->


                // First ?
                statement.setString(
                    1,
                    student.firstName
                )


                // Second ?
                statement.setString(
                    2,
                    student.lastName
                )


                // Third ?
                statement.setString(
                    3,
                    student.email
                )


                /*
                 * Fourth ?
                 *
                 * This is extremely important.
                 *
                 * The WHERE clause determines WHICH student
                 * should be updated.
                 */
                statement.setLong(
                    4,
                    student.id
                )


                statement.executeUpdate()
            }
        }
    }


    // =========================================================
    // DELETE
    // =========================================================

    /**
     * deleteStudent()
     *
     * Deletes a student using their ID.
     *
     * SQL operation:
     *
     * DELETE
     */
    fun deleteStudent(studentId: Long) {

        val sql = """
            DELETE FROM students
            WHERE id = ?;
        """.trimIndent()


        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->


                /*
                 * Replace ? with the ID of the student
                 * we want to delete.
                 */
                statement.setLong(
                    1,
                    studentId
                )


                statement.executeUpdate()
            }
        }
    }
}