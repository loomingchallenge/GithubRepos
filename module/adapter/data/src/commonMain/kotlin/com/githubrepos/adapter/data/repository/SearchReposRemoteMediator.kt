package com.githubrepos.adapter.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.githubrepos.adapter.data.datasource.database.DBWithTransaction
import com.githubrepos.adapter.data.datasource.database.dao.ListRepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoPageDao
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.core.AppDispatchers
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.core.entity.RepoPage
import kotlinx.coroutines.withContext

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
private const val GITHUB_STARTING_PAGE_INDEX = 1

private const val NEXT_PAGE_NOT_EXIST = -1
private const val NEXT_PAGE_CANNOT_BE_FOUND = -2

@OptIn(ExperimentalPagingApi::class)
internal class SearchReposRemoteMediator(
    private val query: String,
    private val service: GithubService,
    private val listRepoDao: ListRepoDao,
    private val repoPageDao: RepoPageDao,
    private val dbWithTransaction: DBWithTransaction,
    private val dispatchers: AppDispatchers
) : RemoteMediator<Int, ListRepo>() {

    private var endPage: Int? = null

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ListRepo>): MediatorResult {
        return withContext(dispatchers.io) {
            val page = getNextPage(loadType, state)
            if (page == NEXT_PAGE_NOT_EXIST) {
                MediatorResult.Success(endOfPaginationReached = true)
            } else {
                try {
                    loadPage(loadType, state, page)
                    MediatorResult.Success(endOfPaginationReached = endPage?.let { page >= it } ?: false)
                } catch (exception: Exception) {
                    MediatorResult.Error(exception)
                }
            }
        }
    }

    private suspend fun loadPage(loadType: LoadType, state: PagingState<Int, ListRepo>, page: Int) {
        val response = service.searchRepos(query, page, state.config.pageSize)
        dbWithTransaction.withTransaction {
            if (loadType == LoadType.REFRESH) {
                listRepoDao.deleteAll()
                repoPageDao.deleteAll()
            }
            if (response.items.isNotEmpty()) {
                listRepoDao.insert(response.items)
                val repoPages = response.items.map {
                    RepoPage(it.id, page)
                }
                repoPageDao.insert(repoPages)
            } else {
                endPage = page - 1
            }
        }
    }

    private fun getNextPage(loadType: LoadType, state: PagingState<Int, ListRepo>): Int =
        when (loadType) {
            LoadType.REFRESH -> {
                endPage = null
                val page = getPageClosestToCurrentPosition(state)
                page ?: GITHUB_STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                when (val page = getPageForFirstItem()) {
                    NEXT_PAGE_CANNOT_BE_FOUND -> GITHUB_STARTING_PAGE_INDEX
                    GITHUB_STARTING_PAGE_INDEX -> NEXT_PAGE_NOT_EXIST
                    else -> page - 1
                }
            }

            LoadType.APPEND -> {
                when (val page = getPageKeyForLastItem()) {
                    NEXT_PAGE_CANNOT_BE_FOUND -> GITHUB_STARTING_PAGE_INDEX
                    endPage -> NEXT_PAGE_NOT_EXIST
                    else -> page + 1
                }
            }
        }

    private fun getPageClosestToCurrentPosition(state: PagingState<Int, ListRepo>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repositoryId ->
                repoPageDao.getRepoPage(repositoryId)?.page
            }
        }
    }

    private fun getPageForFirstItem(): Int {
        return repoPageDao.getMinPage()?.toInt() ?: NEXT_PAGE_CANNOT_BE_FOUND
    }

    private fun getPageKeyForLastItem(): Int {
        return repoPageDao.getMaxPage()?.toInt() ?: NEXT_PAGE_CANNOT_BE_FOUND
    }
}
