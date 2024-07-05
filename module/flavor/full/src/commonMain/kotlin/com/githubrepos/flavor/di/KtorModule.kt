package com.githubrepos.flavor.di

import com.githubrepos.adapter.data.datasource.network.NetworkServiceProvider
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.framework.datasource.network.ktor.di.createNetworkServiceProvider
import org.koin.dsl.module

internal val ktorModule = module {
    single<NetworkServiceProvider>() { createNetworkServiceProvider(true) } // TODO false for RELEASE
    single<GithubService> {
        val networkServiceProvider: NetworkServiceProvider = get()
        networkServiceProvider.createGithubService()
    }
}
