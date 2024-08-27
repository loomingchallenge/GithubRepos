package com.githubrepos.adapter.data.test.datasource.network

import com.githubrepos.adapter.data.datasource.network.NetworkServiceProvider
import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.adapter.data.test.datasource.network.service.FakeGithubService

class FakeNetworkServiceProvider(private val networkStateProvider: NetworkStateProvider): NetworkServiceProvider {

    override fun createGithubService(): GithubService {
        return FakeGithubService(networkStateProvider)
    }
}
