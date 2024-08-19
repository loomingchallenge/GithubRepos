package com.githubrepos.adapter.presentation.di

import com.githubrepos.adapter.presentation.error.ErrorReporter
import com.githubrepos.adapter.presentation.error.ErrorReporterImp
import com.githubrepos.adapter.presentation.viewmodel.RepoListViewModel
import com.githubrepos.adapter.presentation.viewmodel.RepoListViewModelImp
import com.githubrepos.adapter.presentation.viewmodel.RepoViewModel
import com.githubrepos.adapter.presentation.viewmodel.RepoViewModelImp
import com.githubrepos.core.repository.SearchReposRepository
import com.githubrepos.core.usecase.GetRepoUseCase
import kotlinx.coroutines.CoroutineScope

fun createRepoListViewModel(
    scope: CoroutineScope,
    searchRepository: SearchReposRepository,
    errorReporter: ErrorReporter
): RepoListViewModel {
    return RepoListViewModelImp(scope, searchRepository, errorReporter)
}

fun createRepoViewModel(
    repoID: Long,
    title: String,
    scope: CoroutineScope,
    getRepoUseCase: GetRepoUseCase
): RepoViewModel {
    return RepoViewModelImp(repoID, title, scope, getRepoUseCase)
}

fun createErrorReporter(): ErrorReporter {
    return ErrorReporterImp()
}