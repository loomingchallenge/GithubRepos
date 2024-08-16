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

fun Repo.toListRepo() = ListRepo(id, name, fullName, description, stars, forks, language)

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

val repoExample2 = Repo(
    29028775,
    "react-native",
    "facebook/react-native",
    "A framework for building native applications using React",
    "https://github.com/facebook/react-native",
    "https://reactnative.dev",
    "https://github.com/facebook/react-native.git",
    "https://api.github.com/repos/facebook/react-native/deployments",
    113772,
    24144,
    "C++"
)