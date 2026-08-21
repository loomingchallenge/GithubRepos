package com.githubrepos.adapter.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.githubrepos.adapter.data.datasource.database.BaseDatabase
import com.githubrepos.adapter.data.datasource.database.dao.ListRepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoPageDao
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.githubrepos.core.AppDispatchers
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.core.repository.SearchReposRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

private const val NETWORK_PAGE_SIZE = 30

internal class SearchReposRepositoryImp(
    private val service: GithubService,
    private val listRepoDao: ListRepoDao,
    private val repoPageDao: RepoPageDao,
    private val baseDatabase: BaseDatabase,
    private val preferences: RepoPreferences,
    private val dispatchers: AppDispatchers,
) : SearchReposRepository {

    override val lastQuery: String
        get() = preferences.query ?: ""

    override fun getRepositoryFlow(query: String): Flow<PagingData<ListRepo>> {
        val networkQuery = query.ifBlank { "stars:>0" }
        val pagingSourceFactory = { listRepoDao.getListRepos() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = true),
            remoteMediator = SearchReposRemoteMediator(
                networkQuery,
                service,
                listRepoDao,
                repoPageDao,
                baseDatabase,
                dispatchers
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.onStart {
            baseDatabase.init()
            if (preferences.query != query) {
                preferences.query = query
                withContext(dispatchers.io) {
                    baseDatabase.transaction {
                        listRepoDao.deleteAll()
                        repoPageDao.deleteAll()
                    }
                }
            }
        }
    }
}
