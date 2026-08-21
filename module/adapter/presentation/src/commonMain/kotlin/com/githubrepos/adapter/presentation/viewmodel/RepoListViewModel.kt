package com.githubrepos.adapter.presentation.viewmodel

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.githubrepos.adapter.presentation.error.ErrorReporter
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.core.repository.SearchReposRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart

interface RepoListViewModel {

    val isPullRefreshSupported: Boolean

    val repositoryFlow: Flow<PagingData<ListRepo>>

    val searchQuery: StateFlow<String>

    var previousSearchQuery: String

    val isSearchActive: StateFlow<Boolean>

    fun onSearchQueryChanged(query: String)

    fun onSearchActiveChanged(active: Boolean)

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

    override var previousSearchQuery: String = searchRepository.lastQuery

    private val _searchQuery = MutableStateFlow(searchRepository.lastQuery)
    override val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    override val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override val repositoryFlow = _searchQuery
        .flatMapLatest { query ->
            searchRepository.getRepositoryFlow(query)
                .onStart { emit(PagingData.empty()) }
        }
        .cachedIn(scope)

    override fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    override fun onSearchActiveChanged(active: Boolean) {
        _isSearchActive.value = active
    }

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
