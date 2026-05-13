package com.githubrepos.adapter.data.datasource.database.dao

import com.githubrepos.core.entity.RepoPage

interface RepoPageDao {

    suspend fun insert(repoPages: List<RepoPage>)

    suspend fun insertWithTransaction(repoPages: List<RepoPage>)

    suspend fun getRepoPage(repoId: Long): RepoPage?

    suspend fun getMinPage(): Long?

    suspend fun getMaxPage(): Long?

    suspend fun deleteAll()
}
