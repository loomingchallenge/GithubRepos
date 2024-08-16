package com.githubrepos.core.entity

data class RepoPage(
    val repoId: Long,
    val page: Int?
)

val repoPageExample = RepoPage(
    31792824,
    0
)

val repoPageExample2 = RepoPage(
    29028775,
    1
)