package com.githubrepos.core.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Repo(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("description") val description: String?,
    @SerialName("html_url") val url: String?,
    @SerialName("homepage") val homepage: String?,
    @SerialName("clone_url") val cloneUrl: String?,
    @SerialName("deployments_url") val deploymentsUrl: String?,
    @SerialName("stargazers_count") val stars: Int,
    @SerialName("forks_count") val forks: Int,
    @SerialName("language") val language: String?
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