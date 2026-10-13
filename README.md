# Student Grade Tracker

A desktop-based student grade management application built with **Kotlin, Compose Multiplatform Desktop, SQLite, and JDBC**.

The application provides a graphical interface for managing students, courses, and grades while demonstrating relational database programming and SQL integration.

## 1. Project Overview

Student Grade Tracker is a desktop application designed to help users organize student information, manage courses, and record academic grades.

The application uses **Kotlin** for application logic, **Compose Multiplatform Desktop** for the graphical user interface, and **SQLite** for persistent data storage.

Database operations are performed through **JDBC (Java Database Connectivity)**, allowing Kotlin to execute SQL statements, retrieve results, and display database information in the application.

This project demonstrates how a desktop application can interact with a relational database using CRUD operations, SQL JOIN queries, aggregate functions, and data validation.

## 2. Development Environment

| Technology | Purpose |
|---|---|
| Kotlin | Application programming language |
| IntelliJ IDEA | Development environment |
| Compose Multiplatform Desktop | Graphical user interface |
| SQLite | Relational database |
| SQLite JDBC Driver | Database connectivity |
| Gradle | Build and dependency management |
| macOS | Development operating system |

## 3. Application Features

### Student Management
- Add new students.
- View registered students.
- Edit student information.
- Delete student records.
- Validate required student information.

### Course Management
- Add new courses.
- View available courses.
- Edit course information.
- Delete courses.
- Store course codes, names, and credit values.

### Grade Management
- Record grades for students.
- Associate grades with students and courses.
- Edit existing grade records.
- Delete grade records.
- Select students and courses using dropdown menus.
- Display grade records using SQL JOIN queries.

### Dashboard and Statistics
- Display total registered students.
- Display total available courses.
- Display total grade records.
- Calculate average grades using `AVG()`.
- Count database records using `COUNT()`.
- Display course-level grade statistics using `GROUP BY`.

### Validation and Data Safety
- Validate user input before saving records.
- Prevent invalid or incomplete entries.
- Confirm deletion before removing records.
- Maintain relational integrity through foreign keys.

### Data Persistence
- Store application data in SQLite.
- Retrieve saved records when the application starts.
- Preserve data between application sessions.

## 4. Database Structure

The application uses a relational SQLite database containing three tables.

### Students Table

**Table name:** `students`

| Column | Description |
|---|---|
| id | Unique student identifier |
| first_name | Student's first name |
| last_name | Student's last name |
| email | Student's email address |

### Courses Table

**Table name:** `courses`

| Column | Description |
|---|---|
| id | Unique course identifier |
| course_code | Course identification code |
| course_name | Course title |
| credits | Number of course credits |

### Grades Table

**Table name:** `grades`

| Column | Description |
|---|---|
| id | Unique grade identifier |
| student_id | References the students table |
| course_id | References the courses table |
| score | Student's numerical grade |

### Database Relationships

The `grades` table connects students and courses using foreign keys.

- `grades.student_id` references `students.id`.
- `grades.course_id` references `courses.id`.
- One student can have multiple grade records.
- One course can have multiple grade records.

**Relationship diagram:**

```text
STUDENTS
--------
id (PK)
first_name
last_name
email
    |
    | 1-to-many
    |
    v
GRADES
--------
id (PK)
student_id (FK)
course_id (FK)
score
    ^
    |
    | 1-to-many
    |
COURSES
--------
id (PK)
course_code
course_name
credits
```

## 5. SQL Operations

The application demonstrates the following SQL operations:

| SQL Operation | Purpose |
|---|---|
| INSERT | Add new database records |
| SELECT | Retrieve existing records |
| UPDATE | Modify existing records |
| DELETE | Remove records |
| INNER JOIN | Combine related table data |
| COUNT() | Count database records |
| AVG() | Calculate average grades |
| GROUP BY | Group statistics by course |

### Example: Insert a Student

```sql
INSERT INTO students
(first_name, last_name, email)
VALUES ('John', 'Doe', 'john@example.com');
```

### Example: Retrieve Student Grades Using JOIN

```sql
SELECT
    students.first_name,
    students.last_name,
    courses.course_name,
    grades.score
FROM grades
INNER JOIN students
    ON grades.student_id = students.id
INNER JOIN courses
    ON grades.course_id = courses.id;
```

### Example: Calculate Average Grade

```sql
SELECT AVG(score) AS average_grade
FROM grades;
```

### Example: Count Students

```sql
SELECT COUNT(*) AS total_students
FROM students;
```

### Example: Average Grade by Course

```sql
SELECT
    courses.course_name,
    AVG(grades.score) AS average_grade
FROM grades
INNER JOIN courses
    ON grades.course_id = courses.id
GROUP BY courses.id, courses.course_name;
```

These SQL statements are executed through Kotlin repository classes using JDBC.

## 6. Application Architecture

The project separates database operations, application data models, and graphical user interface components.

**Architecture responsibilities:**

- **Data Layer:** Establishes SQLite connections and executes SQL operations.
- **Model Layer:** Represents student, course, and grade information.
- **UI Layer:** Displays records, forms, dropdowns, and dashboard statistics.
- **Repository Layer:** Separates SQL operations from user interface logic.

## 7. Running the Application

### Prerequisites

Install the following:

- IntelliJ IDEA with Kotlin support.
- A compatible Java Development Kit (JDK).
- Gradle dependencies configured through the project wrapper.

### Run Using IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Select **Open Project**.
3. Navigate to the `StudentGradeTracker` project folder.
4. Allow Gradle to synchronize dependencies.
5. Locate the desktop application's main function.
6. Run the application.
7. Add students and courses.
8. Record student grades.
9. Open the Dashboard to view statistics.




## 8. Data Persistence

The application uses a local SQLite database named:

`student_grades.db`

SQLite stores records in a database file rather than temporary application memory.

This allows the application to preserve data after it closes.

### Persistence Verification

1. Launch Student Grade Tracker.
2. Add a student.
3. Add a course.
4. Record a grade.
5. Close the application.
6. Restart the application.
7. Verify that the previously saved records are still displayed.

**Expected result:** Student, course, and grade information remains available after restarting the application, provided the same database file is used.

## 9. Testing and Validation

The application can be tested using the following scenarios:

| Test | Expected Result |
|---|---|
| Add student | Student appears in the list |
| Edit student | Updated information is displayed |
| Delete student | Student record is removed |
| Add course | Course appears in the list |
| Edit course | Course information is updated |
| Delete course | Course record is removed |
| Record grade | Grade is saved in SQLite |
| Edit grade | Updated score is displayed |
| Delete grade | Grade record is removed |
| Invalid input | Validation prevents saving |
| Delete confirmation | User must confirm deletion |
| Dashboard statistics | SQL calculations return correct results |
| Restart application | Saved data remains available |

## 10. Project Purpose

Student Grade Tracker was developed to demonstrate relational database programming using Kotlin and SQLite.

The project focuses on:

- Writing and executing SQL statements.
- Implementing CRUD database operations.
- Connecting Kotlin applications to SQLite using JDBC.
- Designing relational database tables.
- Establishing relationships using foreign keys.
- Retrieving related records using SQL JOIN.
- Calculating statistics using SQL aggregate functions.
- Building a graphical desktop application.
- Validating user input.
- Implementing persistent data storage.

The project demonstrates how database programming concepts can be applied to a practical desktop application.

## 11. Video link
https://youtu.be/Vkunw5Ypfnk


## 12. Conclusion

Student Grade Tracker combines Kotlin, Compose Multiplatform Desktop, SQLite, and JDBC to create a functional desktop database application.

The application demonstrates the integration of SQL operations with a graphical user interface, including data management, relational queries, dashboard statistics, input validation, and persistent storage.

It provides practical experience in relational database development and desktop application programming.