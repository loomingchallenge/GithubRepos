package com.githubrepos.framework.datasource.network.demo_api

import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.test.repoCountInJSON
import com.githubrepos.core.entity.toListRepo
import com.githubrepos.core.test.repo0
import com.githubrepos.core.test.repo1
import com.githubrepos.core.test.repo48
import com.githubrepos.core.test.repo49
import com.githubrepos.framework.datasource.network.demo_api.di.createGithubService
import com.githubrepos.framework.datasource.network.demo_api.di.createNetworkServiceProvider
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GithubServiceTest {

    private val networkStateProvider = NetworkStateProviderImp()
    private val networkServiceProvider = createNetworkServiceProvider(networkStateProvider)
    private val githubService = createGithubService(networkServiceProvider)

    @Test
    fun getRepo() = runTest {
        networkStateProvider.hasNetworkConnection = true

        var repo = githubService.getRepo(repo0.id)
        assertEquals(repo0, repo)

        repo = githubService.getRepo(repo1.id)
        assertEquals(repo1, repo)

        repo = githubService.getRepo(repo48.id)
        assertEquals(repo48, repo)

        repo = githubService.getRepo(repo49.id)
        assertEquals(repo49, repo)

        assertFailsWith<IllegalArgumentException> {
            githubService.getRepo(-1)
        }

        networkStateProvider.hasNetworkConnection = false
        assertFailsWith<NoInternetException> {
            githubService.getRepo(repo0.id)
        }
    }

    @Test
    fun searchRepos() = runTest {
        networkStateProvider.hasNetworkConnection = true

        var response = githubService.searchRepos("", 1, 2)
        assertEquals(repoCountInJSON, response.total)
        assertEquals(2, response.items.size)
        assertEquals(repo0.toListRepo(), response.items[0])
        assertEquals(repo1.toListRepo(), response.items[1])

        response = githubService.searchRepos("", 25, 2)
        assertEquals(repoCountInJSON, response.total)
        assertEquals(2, response.items.size)
        assertEquals(repo48.toListRepo(), response.items[0])
        assertEquals(repo49.toListRepo(), response.items[1])

        networkStateProvider.hasNetworkConnection = false
        assertFailsWith<NoInternetException> {
            githubService.searchRepos("", 1, 2)
        }
    }
}