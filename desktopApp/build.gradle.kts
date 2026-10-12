import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(project(":shared"))

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
// SQLite JDBC driver.
    //
    // Kotlin/JVM cannot communicate with a SQLite database by itself.
    // This library provides a JDBC driver that lets our Kotlin code
    // open and work with SQLite database files.
    implementation("org.xerial:sqlite-jdbc:3.50.3.0")

    implementation("androidx.compose.material3:material3:1.2.1")

        implementation(platform("androidx.compose:compose-bom:2024.10.00"))







}

compose.desktop {
    application {
        mainClass = "com.cse310.studentgradetracker.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.cse310.studentgradetracker"
            packageVersion = "1.0.0"
        }
    }
}