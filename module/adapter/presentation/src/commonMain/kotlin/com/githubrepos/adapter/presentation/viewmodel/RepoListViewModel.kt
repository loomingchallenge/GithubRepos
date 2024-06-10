package com.githubrepos.adapter.presentation.viewmodel

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.core.repository.SearchReposRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RepoListViewModel {
    val repositoryFlow: Flow<PagingData<ListRepo>>
}

internal class RepoListViewModelImp(scope: CoroutineScope, searchRepository: SearchReposRepository) : RepoListViewModel {

    override val repositoryFlow = searchRepository
        .getRepositoryFlow("Android")
        .cachedIn(scope)
}
