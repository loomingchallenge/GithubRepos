package com.githubrepos.framework.datasource.network.demo_api

import com.githubrepos.adapter.data.datasource.network.NetworkServiceProvider
import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.framework.datasource.network.demo_api.service.GithubServiceImp

internal class NetworkServiceProviderImp(private val networkStateProvider: NetworkStateProvider): NetworkServiceProvider {

    override fun createGithubService(): GithubService {
        return GithubServiceImp(networkStateProvider)
    }
}
