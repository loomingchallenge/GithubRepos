package com.githubrepos.framework.datasource.network.ktor.service

import com.githubrepos.adapter.data.datasource.network.entity.RepositorySearchResponse
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.core.entity.Repo
import com.githubrepos.framework.datasource.network.ktor.entity.KtorRepo
import com.githubrepos.framework.datasource.network.ktor.entity.KtorRepositorySearchResponse
import com.githubrepos.framework.datasource.network.ktor.entity.toRepo
import com.githubrepos.framework.datasource.network.ktor.entity.toRepositorySearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class GithubServiceImp(private val client: HttpClient): GithubService {

    override suspend fun searchRepos(query: String, page: Int, itemsPerPage: Int): RepositorySearchResponse {
        return client.get("search/repositories?sort=stars") {
            url {
                parameters.append("q", query)
                parameters.append("page", page.toString())
                parameters.append("per_page", itemsPerPage.toString())
            }
        }.body<KtorRepositorySearchResponse>().toRepositorySearchResponse()
    }

    override suspend fun getRepo(id: Long): Repo {
        return client.get("repositories/$id").body<KtorRepo>().toRepo()
    }
}