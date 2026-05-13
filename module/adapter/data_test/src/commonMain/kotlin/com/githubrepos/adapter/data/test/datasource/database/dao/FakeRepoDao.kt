package com.githubrepos.adapter.data.test.datasource.database.dao

import com.githubrepos.adapter.data.datasource.database.dao.RepoDao
import com.githubrepos.core.entity.Repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeRepoDao: RepoDao {

    private val mapOfFlows = mutableMapOf<Long, MutableStateFlow<Repo?>>()

    override suspend fun insert(repo: Repo) {
        getFlow(repo.id).value = repo
    }

    override suspend fun delete(id: Long) {
        getFlow(id).value = null
    }

    override suspend fun deleteAll() {
        mapOfFlows.keys.forEach { id ->
            delete(id)
        }
    }

    override fun getRepo(id: Long): Flow<Repo?> {
        return getFlow(id)
    }

    private fun getFlow(id: Long): MutableStateFlow<Repo?> {
        return mapOfFlows.getOrPut(id) {
            MutableStateFlow(null)
        }
    }
}