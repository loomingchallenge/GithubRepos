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
