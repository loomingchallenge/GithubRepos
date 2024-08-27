package com.githubrepos.framework.datasource.network.demo_api.di

import com.githubrepos.adapter.data.datasource.network.NetworkServiceProvider
import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.adapter.data.test.datasource.network.FakeNetworkServiceProvider

fun createNetworkServiceProvider(networkStateProvider: NetworkStateProvider): NetworkServiceProvider {
    return FakeNetworkServiceProvider(networkStateProvider)
}

fun createGithubService(networkServiceProvider: NetworkServiceProvider): GithubService {
    return networkServiceProvider.createGithubService()
}