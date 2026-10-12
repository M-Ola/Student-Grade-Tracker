package com.cse310.studentgradetracker.ui


// ============================================================
// COMPOSE IMPORTS
// ============================================================

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

// ============================================================
// APPLICATION IMPORTS
// ============================================================

import com.cse310.studentgradetracker.data.DashboardRepository
import com.cse310.studentgradetracker.model.DashboardStats
import com.cse310.studentgradetracker.model.CourseAverage

/**
 * DashboardScreen
 *
 * Displays SQL-generated statistics:
 *
 * - Total students
 * - Total courses
 * - Total grades
 * - Overall average grade
 * - Average grade per course
 *
 * All values are retrieved from SQLite.
 */
@Composable
fun DashboardScreen() {

    // ========================================================
    // REPOSITORY
    // ========================================================

    val repository = remember {
        DashboardRepository()
    }

    // ========================================================
    // STATE
    // ========================================================

    /*
     * Initially null until the database query succeeds.
     */
    var stats by remember {
        mutableStateOf<DashboardStats?>(null)
    }

    /*
     * Holds results from SQL GROUP BY.
     */
    var courseAverages by remember {
        mutableStateOf<List<CourseAverage>>(emptyList())
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    // ========================================================
    // LOAD DATA
    // ========================================================

    /**
     * Reads fresh statistics from SQLite.
     *
     * When state changes, Compose automatically redraws
     * the dashboard.
     */
    fun refreshDashboard() {

        isLoading = true
        errorMessage = ""

        try {

            stats = repository.getDashboardStats()

            courseAverages = repository.getCourseAverages()

        } catch (exception: Exception) {

            errorMessage =
                "Unable to load dashboard: ${exception.message}"

        } finally {

            isLoading = false
        }
    }

    /*
     * Load dashboard data when the screen is opened.
     *
     * DesktopApp uses when(currentScreen), so navigating
     * to the Dashboard composes this screen.
     */
    LaunchedEffect(Unit) {
        refreshDashboard()
    }

    // ========================================================
    // DASHBOARD LAYOUT
    // ========================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),

        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // ====================================================
        // HEADER
        // ====================================================

        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Overview of student academic performance.",
            style = MaterialTheme.typography.bodyMedium
        )

        /*
         * Refresh button re-executes the SQL queries.
         */
        OutlinedButton(
            onClick = {
                refreshDashboard()
            }
        ) {
            Text("Refresh Dashboard")
        }

        HorizontalDivider()

        // ====================================================
        // LOADING / ERROR STATES
        // ====================================================

        if (isLoading) {

            CircularProgressIndicator()

        } else if (errorMessage.isNotBlank()) {

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )

        } else {

            val currentStats = stats

            if (currentStats != null) {

                // ============================================
                // SUMMARY CARDS
                // ============================================

                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.headlineSmall
                )

                /*
                 * Two cards per row.
                 *
                 * Rows use weight(1f) to divide the
                 * available horizontal space equally.
                 */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    StatCard(
                        title = "Total Students",
                        value = currentStats.totalStudents.toString(),
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        title = "Total Courses",
                        value = currentStats.totalCourses.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    StatCard(
                        title = "Recorded Grades",
                        value = currentStats.totalGrades.toString(),
                        modifier = Modifier.weight(1f)
                    )

                    /*
                     * Display a dash when AVG() returned NULL.
                     */
                    val averageText =
                        currentStats.averageGrade?.let {
                            "${formatOneDecimal(it)}%"
                        } ?: "—"

                    StatCard(
                        title = "Average Grade",
                        value = averageText,
                        modifier = Modifier.weight(1f)
                    )
                }

                HorizontalDivider()

                // ============================================
                // COURSE AVERAGES
                // ============================================

                Text(
                    text = "Average Grade by Course",
                    style = MaterialTheme.typography.headlineSmall
                )

                if (courseAverages.isEmpty()) {

                    Text(
                        text = "No grades have been recorded yet."
                    )

                } else {

                    /*
                     * Each course has its own average
                     * calculated using SQL AVG().
                     */
                    courseAverages.forEach { course ->

                        CourseAverageCard(course)
                    }
                }
            }
        }
    }
}

// ============================================================
// SUMMARY CARD
// ============================================================

/**
 * StatCard
 *
 * Reusable card for displaying one dashboard statistic.
 */
@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================================
// COURSE AVERAGE CARD
// ============================================================

/**
 * CourseAverageCard
 *
 * Displays the result of SQL AVG() and COUNT()
 * for one course.
 */
@Composable
private fun CourseAverageCard(
    course: CourseAverage
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // Course code and name.
            Text(
                text = "${course.courseCode} — ${course.courseName}",
                style = MaterialTheme.typography.titleMedium
            )

            // Number of grade records in this course.
            Text(
                text = "Recorded grades: ${course.totalGrades}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Average score calculated by SQLite.
            Text(
                text = "Average: ${formatOneDecimal(course.averageScore)}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            /*
             * LinearProgressIndicator accepts a value
             * between 0.0 and 1.0.
             *
             * Our scores range from 0 to 100.
             *
             * Therefore, divide the score by 100.
             */
            val progress =
                (course.averageScore / 100.0)
                    .toFloat()
                    .coerceIn(0f, 1f)

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Formats a Double with one decimal place.
 *
 * Uses arithmetic rather than JVM-only String.format,
 * keeping the logic simple.
 */
private fun formatOneDecimal(value: Double): String {

    val rounded =
        kotlin.math.round(value * 10.0) / 10.0

    return rounded.toString()
}
