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
private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
internal class SearchReposRemoteMediator(
    private val query: String,
    private val service: GithubService,
    private val listRepoDao: ListRepoDao,
    private val repoPageDao: RepoPageDao,
    private val dbWithTransaction: DBWithTransaction,
    private val dispatchers: AppDispatchers
) : RemoteMediator<Int, ListRepo>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ListRepo>): MediatorResult {
        return withContext(dispatchers.io) {
            val pagesToLoad = getPagesToLoad(loadType, state)
            if (loadType == LoadType.PREPEND && pagesToLoad.first < STARTING_PAGE_INDEX) {
                MediatorResult.Success(endOfPaginationReached = true)
            } else {
                try {
                    val loadedPagesCount = loadPages(loadType, state, pagesToLoad)
                    val endOfPaginationReached = (loadedPagesCount == 0)
                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                } catch (exception: Exception) {
                    MediatorResult.Error(exception)
                }
            }
        }
    }

    private suspend fun loadPages(loadType: LoadType, state: PagingState<Int, ListRepo>, pagesToLoad: IntRange): Int {
        val pageResponses = pagesToLoad.map { page ->
            Pair(page, service.searchRepos(query, page, state.config.pageSize))
        }.toList()

        var loadedPagesCount = 0
        dbWithTransaction.withTransaction {
            if (loadType == LoadType.REFRESH) {
                listRepoDao.deleteAll()
                repoPageDao.deleteAll()
            }
            for ((page, response) in pageResponses) {
                if (response.items.isNotEmpty()) {
                    listRepoDao.insert(response.items)
                    val repoPages = response.items.map { item ->
                        RepoPage(item.id, page)
                    }
                    repoPageDao.insert(repoPages)
                    loadedPagesCount++
                }
            }
        }
        return loadedPagesCount
    }

    private fun getPagesToLoad(loadType: LoadType, state: PagingState<Int, ListRepo>): IntRange =
        when (loadType) {
            LoadType.REFRESH -> {
                getAnchorPagesToLoad(state) ?: STARTING_PAGE_INDEX..(state.config.initialLoadSize / state.config.pageSize)
            }

            LoadType.PREPEND -> {
                when (val page = getPageForFirstItem()) {
                    null -> STARTING_PAGE_INDEX.toRange()
                    else -> (page - 1).toRange()
                }
            }

            LoadType.APPEND -> {
                when (val page = getPageForLastItem()) {
                    null -> STARTING_PAGE_INDEX.toRange()
                    else -> (page + 1).toRange()
                }
            }
        }

    private fun getAnchorPagesToLoad(state: PagingState<Int, ListRepo>): IntRange? {
        val anchor = state.anchorPosition ?: return null
        val pageSize = state.config.pageSize
        val initialLoadSize = state.config.initialLoadSize
        val pageOffset = maxOf(1, ((initialLoadSize / pageSize) - 1) / 2)

        val anchorItem = state.closestItemToPosition(anchor) ?: return null
        val anchorPage = repoPageDao.getRepoPage(anchorItem.id)?.page ?: return null

        val startPage = maxOf(STARTING_PAGE_INDEX, anchorPage - pageOffset)
        val endPage = maxOf(STARTING_PAGE_INDEX + 1,anchorPage + pageOffset)

        return startPage..endPage
    }

    private fun getPageForFirstItem(): Int? {
        return repoPageDao.getMinPage()?.toInt()
    }

    private fun getPageForLastItem(): Int? {
        return repoPageDao.getMaxPage()?.toInt()
    }
}

fun Int.toRange(): IntRange = this..this