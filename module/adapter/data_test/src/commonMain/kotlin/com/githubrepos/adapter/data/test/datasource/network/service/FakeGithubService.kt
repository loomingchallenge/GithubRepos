package com.githubrepos.adapter.data.test.datasource.network.service

import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.datasource.network.entity.RepositorySearchResponse
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.test.generateListRepos

class FakeGithubService(
    private val networkStateProvider: NetworkStateProvider,
    private val listRepos: List<ListRepo> = generateListRepos(50)
): GithubService {

    override suspend fun searchRepos(query: String, page: Int, itemsPerPage: Int): RepositorySearchResponse {
        if (networkStateProvider.hasNetworkConnection()) {
            val index = (page - 1) * itemsPerPage
            return RepositorySearchResponse(
                listRepos.count(),
                if (index < listRepos.count()) listRepos.subList(index, index + itemsPerPage) else listOf()
            )
        } else {
            throw NoInternetException()
        }
    }

    override suspend fun getRepo(id: Long): Repo {
        if (networkStateProvider.hasNetworkConnection()) {
            val listRepo: ListRepo = listRepos.find { it.id == id }
                ?: throw IllegalArgumentException("Repo with id $id not found")
            return Repo(
                id = listRepo.id,
                name = listRepo.name,
                fullName = listRepo.fullName,
                description = listRepo.description,
                url = "https://api.github.com/repos/flutter/flutter",
                homepage = "https://flutter.dev",
                cloneUrl = "https://github.com/facebook/react-native.git",
                deploymentsUrl = "https://api.github.com/repos/flutter/flutter/deployments",
                stars = listRepo.stars,
                forks = listRepo.forks,
                language = listRepo.language
            )
        } else {
            throw NoInternetException()
        }
    }
}
