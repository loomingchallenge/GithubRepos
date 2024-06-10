package com.githubrepos.adapter.presentation

import com.githubrepos.adapter.presentation.viewmodel.RepoListViewModel
import com.githubrepos.adapter.presentation.viewmodel.RepoViewModel
import kotlinx.coroutines.CoroutineScope

interface ViewModelProvider {

    fun createRepoViewModel(repoID: Long, title: String, scope: CoroutineScope): RepoViewModel

    fun createRepoListViewModel(scope: CoroutineScope): RepoListViewModel
}
