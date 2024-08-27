package com.githubrepos.`build-logic`

import org.gradle.api.Project

fun Project.isTestTask(): Boolean {
    val taskNames = gradle.startParameter.taskNames
    return taskNames.any { it.contains("Test", ignoreCase = true) }
}