package com.githubrepos.adapter.data.test.datasource.network.service

import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.test.datasource.network.FakeJsonNetworkServiceProvider
import com.githubrepos.adapter.data.test.datasource.network.FakeNetworkStateProvider
import com.githubrepos.adapter.data.test.resources.repoCountInJSON
import com.githubrepos.core.entity.toListRepo
import com.githubrepos.core.test.repo1
import com.githubrepos.core.test.repo2
import com.githubrepos.core.test.repo49
import com.githubrepos.core.test.repo50
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FakeJsonGithubServiceTest {

    private val networkStateProvider = FakeNetworkStateProvider()
    private val networkServiceProvider = FakeJsonNetworkServiceProvider(networkStateProvider)
    private val githubService = networkServiceProvider.createGithubService()

    @Test
    fun getRepo() = runTest {
        networkStateProvider.hasNetworkConnection = true

        var repo = githubService.getRepo(repo1.id)
        assertEquals(repo1, repo)

        repo = githubService.getRepo(repo2.id)
        assertEquals(repo2, repo)

        repo = githubService.getRepo(repo49.id)
        assertEquals(repo49, repo)

        repo = githubService.getRepo(repo50.id)
        assertEquals(repo50, repo)

        assertFailsWith<IllegalArgumentException> {
            githubService.getRepo(-1)
        }

        networkStateProvider.hasNetworkConnection = false
        assertFailsWith<NoInternetException> {
            githubService.getRepo(repo1.id)
        }
    }

    @Test
    fun searchRepos() = runTest {
        networkStateProvider.hasNetworkConnection = true

        var response = githubService.searchRepos("", 1, 2)
        assertEquals(repoCountInJSON, response.total)
        assertEquals(2, response.items.size)
        assertEquals(repo1.toListRepo(), response.items[0])
        assertEquals(repo2.toListRepo(), response.items[1])

        response = githubService.searchRepos("", 25, 2)
        assertEquals(repoCountInJSON, response.total)
        assertEquals(2, response.items.size)
        assertEquals(repo49.toListRepo(), response.items[0])
        assertEquals(repo50.toListRepo(), response.items[1])

        networkStateProvider.hasNetworkConnection = false
        assertFailsWith<NoInternetException> {
            githubService.searchRepos("", 1, 2)
        }
    }
}