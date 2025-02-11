package com.githubrepos.adapter.presentation.viewmodel

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.githubrepos.adapter.presentation.error.ErrorReporter
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.core.repository.SearchReposRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RepoListViewModel {

    val isPullRefreshSupported: Boolean

    val repositoryFlow: Flow<PagingData<ListRepo>>

    suspend fun setRepositoryError(error: Throwable)

    fun clearRepositoryError()
}

internal class RepoListViewModelImp(
    scope: CoroutineScope,
    searchRepository: SearchReposRepository,
    private val errorReporter: ErrorReporter,
    override val isPullRefreshSupported: Boolean
) : RepoListViewModel {

    private var repositoryError: Throwable? = null

    override val repositoryFlow = searchRepository
        .getRepositoryFlow("Android")
        .cachedIn(scope)

    override suspend fun setRepositoryError(error: Throwable) {
        if (error != repositoryError) {
            repositoryError = error
            errorReporter.setError(exc = error)
        }
    }

    override fun clearRepositoryError() {
        repositoryError = null
    }
}
