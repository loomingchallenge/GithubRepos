package com.githubrepos.framework.ui.screens.viewmodel

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.githubrepos.adapter.presentation.viewmodel.RepoListViewModel
import com.githubrepos.core.entity.ListRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeRepoListViewModel(
    override val isPullRefreshSupported: Boolean
): RepoListViewModel {
    private val _repositoryFlow = MutableStateFlow<PagingData<ListRepo>>(PagingData.empty())

    override val repositoryFlow: Flow<PagingData<ListRepo>> = _repositoryFlow.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    override val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    override val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    override fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    override fun onSearchActiveChanged(active: Boolean) {
        _isSearchActive.value = active
    }

    var repositoryError: Throwable? = null
        private set

    override suspend fun setRepositoryError(error: Throwable) {
        repositoryError = error
    }

    override fun clearRepositoryError() {
        repositoryError = null
    }

    fun emitListRepo(
        items: List<ListRepo> = emptyList(),
        refresh: LoadState = LoadState.NotLoading(true),
        prepend: LoadState = LoadState.NotLoading(endOfPaginationReached = true),
        append: LoadState = LoadState.NotLoading(endOfPaginationReached = true)
    ) {
        _repositoryFlow.value = PagingData.from(
            data = items,
            sourceLoadStates = LoadStates(
                refresh = refresh,
                prepend = prepend,
                append = append
            )
        )
    }
}