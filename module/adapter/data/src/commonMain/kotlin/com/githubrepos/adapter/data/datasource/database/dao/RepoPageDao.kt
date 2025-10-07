package com.githubrepos.adapter.data.datasource.database.dao

import com.githubrepos.core.entity.RepoPage

interface RepoPageDao {

    fun insert(repoPages: List<RepoPage>)

    fun getRepoPage(repoId: Long): RepoPage?

    fun getMinPage(): Long?

    fun getMaxPage(): Long?

    fun deleteAll()
}
