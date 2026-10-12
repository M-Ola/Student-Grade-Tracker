package com.cse310.studentgradetracker.data



import com.cse310.studentgradetracker.model.Course


/**
 * CourseRepository
 *
 * Handles all SQL operations involving courses.
 *
 * This repository gives us CRUD operations:
 *
 * CREATE -> INSERT
 * READ   -> SELECT
 * UPDATE -> UPDATE
 * DELETE -> DELETE
 */
class CourseRepository {


    // =========================================================
    // CREATE
    // =========================================================

    /**
     * Inserts a new course into SQLite.
     */
    fun addCourse(course: Course) {

        val sql = """
            INSERT INTO courses (
                course_code,
                course_name,
                credits
            )
            VALUES (?, ?, ?);
        """.trimIndent()


        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->


                /*
                 * First ? = course code
                 */
                statement.setString(
                    1,
                    course.courseCode
                )


                /*
                 * Second ? = course name
                 */
                statement.setString(
                    2,
                    course.courseName
                )


                /*
                 * Third ? = number of credits
                 */
                statement.setInt(
                    3,
                    course.credits
                )


                /*
                 * INSERT changes database data, so we use
                 * executeUpdate().
                 */
                statement.executeUpdate()
            }
        }
    }


    // =========================================================
    // READ
    // =========================================================

    /**
     * Retrieves every course from SQLite.
     */
    fun getAllCourses(): List<Course> {

        /*
         * Temporary mutable list where we will place
         * Course objects created from the ResultSet.
         */
        val courses = mutableListOf<Course>()


        val sql = """
            SELECT
                id,
                course_code,
                course_name,
                credits
            FROM courses
            ORDER BY course_code;
        """.trimIndent()


        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->


                /*
                 * executeQuery() sends our SELECT statement
                 * to SQLite.
                 *
                 * SQLite returns a ResultSet.
                 */
                statement.executeQuery().use { resultSet ->


                    /*
                     * Move through every returned database row.
                     */
                    while (resultSet.next()) {


                        /*
                         * Convert the current SQLite row into
                         * a Kotlin Course object.
                         */
                        val course = Course(

                            id =
                                resultSet.getLong("id"),

                            courseCode =
                                resultSet.getString(
                                    "course_code"
                                ),

                            courseName =
                                resultSet.getString(
                                    "course_name"
                                ),

                            credits =
                                resultSet.getInt("credits")
                        )


                        /*
                         * Store the Course object in our list.
                         */
                        courses.add(course)
                    }
                }
            }
        }


        /*
         * Return the database results to the caller.
         */
        return courses
    }


    // =========================================================
    // UPDATE
    // =========================================================

    /**
     * Updates an existing course.
     */
    fun updateCourse(course: Course) {

        val sql = """
            UPDATE courses
            SET
                course_code = ?,
                course_name = ?,
                credits = ?
            WHERE id = ?;
        """.trimIndent()


        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->


                statement.setString(
                    1,
                    course.courseCode
                )


                statement.setString(
                    2,
                    course.courseName
                )


                statement.setInt(
                    3,
                    course.credits
                )


                /*
                 * The ID determines which course gets updated.
                 */
                statement.setLong(
                    4,
                    course.id
                )


                statement.executeUpdate()
            }
        }
    }


    // =========================================================
    // DELETE
    // =========================================================

    /**
     * Deletes a course using its primary key.
     */
    fun deleteCourse(courseId: Long) {

        val sql = """
            DELETE FROM courses
            WHERE id = ?;
        """.trimIndent()


        Database.connect().use { connection ->

            connection.prepareStatement(sql).use { statement ->


                statement.setLong(
                    1,
                    courseId
                )


                statement.executeUpdate()
            }
        }
    }
}