package com.githubrepos.core.entity

data class Repo(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val url: String?,
    val homepage: String?,
    val cloneUrl: String?,
    val deploymentsUrl: String?,
    val stars: Int,
    val forks: Int,
    val language: String?
)

val repoExample = Repo(
    31792824,
    "flutter",
    "flutter/flutter",
    "Flutter makes it easy and fast to build beautiful apps for mobile and beyond",
    "https://github.com/flutter",
    "https://flutter.dev",
    "https://github.com/flutter/flutter.git",
    "https://api.github.com/repos/flutter/flutter/deployments",
    155489,
    25786,
    "Dart"
)

fun Repo.toListRepo() = ListRepo(id, name, fullName, description, stars, forks, language)