package com.githubrepos.adapter.data.datasource.network.service

import com.githubrepos.adapter.data.datasource.network.entity.RepositorySearchResponse
import com.githubrepos.core.entity.Repo

const val IN_QUALIFIER = "in:name,description"

interface GithubService {

    fun searchRepos(
        query: String,
        page: Int,
        itemsPerPage: Int
    ): RepositorySearchResponse

    fun getRepo(id: Long): Repo
}
