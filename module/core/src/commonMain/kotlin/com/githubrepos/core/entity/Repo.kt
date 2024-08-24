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