package com.githubrepos.adapter.data.test.datasource.network.service

import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.datasource.network.entity.RepositorySearchResponse
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.adapter.data.test.reposJSON
import com.githubrepos.core.entity.Repo
import com.githubrepos.adapter.data.test.datasource.network.entity.FakeRepositorySearchResponse
import com.githubrepos.adapter.data.test.datasource.network.entity.getPageData
import com.githubrepos.adapter.data.test.datasource.network.entity.getRepo
import kotlinx.serialization.json.Json

internal class FakeGithubService(private val networkStateProvider: NetworkStateProvider): GithubService {

    private val response by lazy {
        val json = Json {
            ignoreUnknownKeys = true
        }
        json.decodeFromString<FakeRepositorySearchResponse>(reposJSON)
    }

    override suspend fun searchRepos(query: String, page: Int, itemsPerPage: Int): RepositorySearchResponse {
        if (networkStateProvider.hasNetworkConnection()) {
            return response.getPageData(page, itemsPerPage)
        } else {
            throw NoInternetException()
        }
    }

    override suspend fun getRepo(id: Long): Repo {
        if (networkStateProvider.hasNetworkConnection()) {
            return response.getRepo(id)
        } else {
            throw NoInternetException()
        }
    }
}
