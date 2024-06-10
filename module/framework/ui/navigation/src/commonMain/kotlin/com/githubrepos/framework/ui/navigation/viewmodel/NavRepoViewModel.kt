package com.githubrepos.framework.ui.navigation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githubrepos.adapter.presentation.ViewModelProvider
import com.githubrepos.adapter.presentation.viewmodel.RepoViewModel
import com.githubrepos.core.LceFlows
import com.githubrepos.core.entity.Repo

internal class NavRepoViewModel(
    repoID: Long,
    title: String,
    viewModelProvider: ViewModelProvider
) : ViewModel(), RepoViewModel {

    private val viewModel = viewModelProvider.createRepoViewModel(repoID, title, viewModelScope)

    override val title: String
        get() = viewModel.title

    override val repoLceFlows: LceFlows<Repo>
        get() = viewModel.repoLceFlows

    override fun loadRepo(refresh: Boolean) {
        viewModel.loadRepo(refresh)
    }
}
