package com.githubrepos.core.entity

data class ListRepo(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String?
)

val listRepoExample = ListRepo(
    0,
    "okhttp",
    "square/okhttp",
    "OkHttp is an efficient HTTP & HTTP/2 client for Android and Java applications.",
    44179,
    10311,
    "Kotlin"
)
