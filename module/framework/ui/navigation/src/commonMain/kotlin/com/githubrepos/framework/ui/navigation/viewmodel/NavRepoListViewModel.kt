package com.githubrepos.framework.ui.navigation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.githubrepos.adapter.presentation.ViewModelProvider
import com.githubrepos.adapter.presentation.viewmodel.RepoListViewModel
import com.githubrepos.core.entity.ListRepo
import kotlinx.coroutines.flow.Flow

internal class NavRepoListViewModel(
    viewModelProvider: ViewModelProvider
) : ViewModel(), RepoListViewModel {

    private val viewModel = viewModelProvider.createRepoListViewModel(viewModelScope)

    override val repositoryFlow: Flow<PagingData<ListRepo>>
        get() = viewModel.repositoryFlow
}
