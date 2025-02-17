package com.githubrepos.`build-logic`

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

fun Project.isTestTask(): Boolean {
    val taskNames = gradle.startParameter.taskNames
    return taskNames.any { it.contains("Test", ignoreCase = true) }
}

// TODO Remove when the issue is fixed: https://youtrack.jetbrains.com/issue/CMP-6612/Support-non-compose-UI-tests-with-resources
fun Project.disableAndroidTests() {
    tasks.withType<Test>().configureEach {
        if (name.contains("testDebugUnitTest", ignoreCase = true) ||
            name.contains("testReleaseUnitTest", ignoreCase = true)
        ) {
            enabled = false
            println("Disabling Android tests in module: ${project.name} because of issue " +
                    "https://youtrack.jetbrains.com/issue/CMP-6612/Support-non-compose-UI-tests-with-resources")
        }
    }
}