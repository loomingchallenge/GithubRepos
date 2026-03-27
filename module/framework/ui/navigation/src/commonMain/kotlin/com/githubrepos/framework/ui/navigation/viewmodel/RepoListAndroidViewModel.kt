package com.githubrepos.framework.ui.navigation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githubrepos.adapter.presentation.ViewModelProvider

internal class RepoListAndroidViewModel(
    viewModelProvider: ViewModelProvider
) : ViewModel() {

    val viewModel = viewModelProvider.createRepoListViewModel(viewModelScope)
}
