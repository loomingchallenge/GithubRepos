package com.githubrepos.framework.datasource.network.demo_api.service

import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.datasource.network.entity.RepositorySearchResponse
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.adapter.data.test.reposJSON
import com.githubrepos.core.entity.Repo
import com.githubrepos.framework.datasource.network.demo_api.entity.DemoRepositorySearchResponse
import kotlinx.serialization.json.Json

internal class GithubServiceImp(private val networkStateProvider: NetworkStateProvider): GithubService {

    private var response: DemoRepositorySearchResponse? = null

    override suspend fun searchRepos(query: String, page: Int, itemsPerPage: Int): RepositorySearchResponse {
        val response = getDemoResponse()
        if (networkStateProvider.hasNetworkConnection()) {
            return response.getPageData(page, itemsPerPage)
        } else {
            throw NoInternetException()
        }
    }

    override suspend fun getRepo(id: Long): Repo {
        val response = getDemoResponse()
        if (networkStateProvider.hasNetworkConnection()) {
            return response.getRepo(id)
        } else {
            throw NoInternetException()
        }
    }

    private fun getDemoResponse(): DemoRepositorySearchResponse {
        if (response == null) {
            val json = Json {
                ignoreUnknownKeys = true
            }
            response = json.decodeFromString<DemoRepositorySearchResponse>(reposJSON)
        }
        return response as DemoRepositorySearchResponse
    }
}
