package com.githubrepos.di

import com.githubrepos.framework.datasource.network.demo_api.di.createGithubService
import com.githubrepos.framework.datasource.network.demo_api.di.createNetworkServiceProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val demoApiModule = module {
    singleOf(::createNetworkServiceProvider)
    singleOf(::createGithubService)
}
