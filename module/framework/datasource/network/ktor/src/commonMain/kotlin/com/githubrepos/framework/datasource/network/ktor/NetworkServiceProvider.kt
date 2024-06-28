package com.githubrepos.framework.datasource.network.ktor

import com.githubrepos.adapter.data.datasource.network.NetworkServiceProvider
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.framework.datasource.network.ktor.service.GithubServiceImp
import io.ktor.client.HttpClient

internal class NetworkServiceProviderImp(private val httpClient: HttpClient): NetworkServiceProvider {

    override fun createGithubService(): GithubService {
        return GithubServiceImp(httpClient)
    }
}
