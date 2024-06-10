package com.githubrepos.adapter.data.datasource.database.dao

import com.githubrepos.core.entity.Repo
import kotlinx.coroutines.flow.Flow

interface RepoDao {

    fun insert(repo: Repo)

    fun delete(id: Long)

    fun deleteAll()

    fun getRepo(id: Long): Flow<Repo?>
}
