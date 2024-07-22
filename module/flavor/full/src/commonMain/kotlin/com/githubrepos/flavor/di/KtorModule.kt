package com.githubrepos.flavor.di

import com.githubrepos.adapter.data.datasource.network.NetworkServiceProvider
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.framework.datasource.network.ktor.di.createNetworkServiceProvider
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun createKtorModule(isDebug: Boolean): Module {
    return module {
        single<NetworkServiceProvider> { createNetworkServiceProvider(isDebug) }
        single<GithubService> {
            val networkServiceProvider: NetworkServiceProvider = get()
            networkServiceProvider.createGithubService()
        }
    }
}
