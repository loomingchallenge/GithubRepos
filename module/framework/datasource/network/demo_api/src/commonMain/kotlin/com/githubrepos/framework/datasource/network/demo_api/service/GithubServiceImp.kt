package com.githubrepos.framework.datasource.network.demo_api.service

import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.datasource.network.entity.RepositorySearchResponse
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.core.entity.Repo
import com.githubrepos.framework.datasource.network.demo_api.entity.DemoRepositorySearchResponse
import githubrepos.module.framework.datasource.network.demo_api.generated.resources.Res.readBytes
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

internal class GithubServiceImp(private val networkStateProvider: NetworkStateProvider): GithubService {

    private var response: DemoRepositorySearchResponse? = null

    override fun searchRepos(query: String, page: Int, itemsPerPage: Int): RepositorySearchResponse {
        val response = getDemoResponse()
        if (networkStateProvider.hasNetworkConnection()) {
            return response.getPageData(page, itemsPerPage)
        } else {
            throw NoInternetException()
        }

    }

    override fun getRepo(id: Long): Repo {
        val response = getDemoResponse()
        if (networkStateProvider.hasNetworkConnection()) {
            return response.getRepo(id)
        } else {
            throw NoInternetException()
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private fun getDemoResponse(): DemoRepositorySearchResponse {
        if (response == null) {
            runBlocking {
                val bytes = readBytes("files/repos.json")
                val str = bytes.decodeToString()
                val json = Json {
                    ignoreUnknownKeys = true
                }
                response = json.decodeFromString<DemoRepositorySearchResponse>(str)
            }
        }
        return response as DemoRepositorySearchResponse
    }
}
