package com.githubrepos.core.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListRepo(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("description") val description: String?,
    @SerialName("stargazers_count") val stars: Int,
    @SerialName("forks_count") val forks: Int,
    @SerialName("language") val language: String?
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
