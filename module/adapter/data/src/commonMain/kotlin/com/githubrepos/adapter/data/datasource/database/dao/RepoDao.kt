package com.githubrepos.adapter.data.datasource.database.dao

import com.githubrepos.core.entity.Repo
import kotlinx.coroutines.flow.Flow

interface RepoDao {

    suspend fun insert(repo: Repo)

    suspend fun delete(id: Long)

    suspend fun deleteAll()

    fun getRepo(id: Long): Flow<Repo?>
}
