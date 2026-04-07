package com.githubrepos.flavor.di

import com.githubrepos.framework.datasource.network.demo_api.di.createGithubService
import com.githubrepos.framework.datasource.network.demo_api.di.createNetworkServiceProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun createApiModule(isDebug: Boolean): Module {
    return module {
        singleOf(::createNetworkServiceProvider)
        singleOf(::createGithubService)
    }
}