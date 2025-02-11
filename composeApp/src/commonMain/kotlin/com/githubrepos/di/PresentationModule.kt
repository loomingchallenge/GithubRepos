package com.githubrepos.di

import com.githubrepos.adapter.presentation.ViewModelProvider
import com.githubrepos.adapter.presentation.di.createErrorReporter
import com.githubrepos.adapter.presentation.di.createRepoListViewModel
import com.githubrepos.adapter.presentation.di.createRepoViewModel
import com.githubrepos.adapter.presentation.viewmodel.RepoListViewModel
import com.githubrepos.adapter.presentation.viewmodel.RepoViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

fun createPresentationModule(isPullRefreshSupported: Boolean) = module {
    singleOf(::createErrorReporter)
    factory { (scope: CoroutineScope) -> createRepoListViewModel(scope, get(), get(), isPullRefreshSupported) }
    factory { (repoID: Long, title: String, scope: CoroutineScope) -> createRepoViewModel(repoID, title, scope, get()) }
    singleOf(::ViewModelProviderImp)  { bind<ViewModelProvider>() }
}

class ViewModelProviderImp: ViewModelProvider, KoinComponent {

    override fun createRepoViewModel(repoID: Long, title: String, scope: CoroutineScope): RepoViewModel {
        return get<RepoViewModel> { parametersOf(repoID, title, scope) }
    }

    override fun createRepoListViewModel(scope: CoroutineScope): RepoListViewModel {
       return get<RepoListViewModel> { parametersOf(scope) }
    }
}