package com.githubrepos.adapter.data.datasource.network.adapter

import com.githubrepos.adapter.data.datasource.network.entity.RepositorySearchResponse
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.core.entity.Repo

/*
* Example adapter. (Currently not used )
* Adapter converts values from core layer to framework layer and back.
* Can be used in SearchReposRepositoryImp and RepoStoreImp instead of GithubService.
* createRepoStore in DataModule file must be changed.
*/
class GithubServiceAdapter(private val githubService: GithubService)  {

    suspend fun searchRepos(
        query: String,
        page: Int,
        itemsPerPage: Int
    ): RepositorySearchResponse {
        // Convert input values from core to framework values.
        // Call framework function.
        val response = githubService.searchRepos(query, page, itemsPerPage)
        // Convert output from framework value to core value.
        return response
    }

    suspend fun getRepo(id: Long): Repo {
        return githubService.getRepo(id)
    }
}
