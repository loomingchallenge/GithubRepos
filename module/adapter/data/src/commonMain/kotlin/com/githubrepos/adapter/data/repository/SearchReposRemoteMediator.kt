package com.githubrepos.adapter.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.githubrepos.adapter.data.datasource.database.RepoDatabase
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
    private val repoDatabase: RepoDatabase,
    private val dispatchers: AppDispatchers
) : RemoteMediator<Int, ListRepo>() {

    private var lastPage: Int? = null

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH // InitializeAction.LAUNCH_INITIAL_REFRESH - if data should be loaded from API first time after creation
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ListRepo>): MediatorResult {
        return withContext(dispatchers.io) {
            if ((loadType == LoadType.PREPEND || loadType == LoadType.APPEND) && arePagesEmpty(state)) {
                MediatorResult.Success(endOfPaginationReached = false)
            } else {
                val page = getNextPage(loadType, state)
                if (page == NEXT_PAGE_NOT_EXIST || page == NEXT_PAGE_CANNOT_BE_FOUND) {
                    MediatorResult.Success(endOfPaginationReached = page == NEXT_PAGE_NOT_EXIST)
                } else {
                    try {
                        loadPage(loadType, state, page)
                        MediatorResult.Success(endOfPaginationReached = lastPage?.let { page >= it } ?: false)
                    } catch (exception: Exception) {
                        MediatorResult.Error(exception)
                    }
                }
            }
        }
    }

    private suspend fun loadPage(loadType: LoadType, state: PagingState<Int, ListRepo>, page: Int) {
        val response = service.searchRepos(query, page, state.config.pageSize)
        repoDatabase.withTransaction {
            if (loadType == LoadType.REFRESH) {
                repoDatabase.listRepoDao.deleteAll()
                repoDatabase.repoPageDao.deleteAll()
            }
            if (response.items.isNotEmpty()) {
                repoDatabase.listRepoDao.insert(response.items)
                val repoPages = response.items.map {
                    RepoPage(it.id, page)
                }
                repoDatabase.repoPageDao.insert(repoPages)
            } else {
                lastPage = page - 1
            }
        }
    }

    private fun getNextPage(loadType: LoadType, state: PagingState<Int, ListRepo>): Int =
        when (loadType) {
            LoadType.REFRESH -> {
                lastPage = null
                val page = getPageClosestToCurrentPosition(state)
                page ?: GITHUB_STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                when (val page = getPageForFirstItem(state)) {
                    NEXT_PAGE_CANNOT_BE_FOUND -> NEXT_PAGE_CANNOT_BE_FOUND
                    GITHUB_STARTING_PAGE_INDEX -> NEXT_PAGE_NOT_EXIST
                    else -> page - 1
                }
            }

            LoadType.APPEND -> {
                when (val page = getPageKeyForLastItem(state)) {
                    NEXT_PAGE_CANNOT_BE_FOUND -> NEXT_PAGE_CANNOT_BE_FOUND
                    lastPage -> NEXT_PAGE_NOT_EXIST
                    else -> page + 1
                }
            }
        }

    private fun arePagesEmpty(state: PagingState<Int, ListRepo>) =
        state.pages.firstOrNull { it.data.isNotEmpty() } == null

    private fun getPageClosestToCurrentPosition(state: PagingState<Int, ListRepo>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repositoryId ->
                repoDatabase.repoPageDao.getRepoPage(repositoryId)?.page
            }
        }
    }

    private fun getPageForFirstItem(state: PagingState<Int, ListRepo>): Int {
        state.pages.first { it.data.isNotEmpty() }.data.first().let { repository ->
            return repoDatabase.repoPageDao.getRepoPage(repository.id)?.page
                ?: NEXT_PAGE_CANNOT_BE_FOUND
        }
    }

    private fun getPageKeyForLastItem(state: PagingState<Int, ListRepo>): Int {
        state.pages.last { it.data.isNotEmpty() }.data.last().let { repository ->
            return repoDatabase.repoPageDao.getRepoPage(repository.id)?.page
                ?: NEXT_PAGE_CANNOT_BE_FOUND
        }
    }
}
