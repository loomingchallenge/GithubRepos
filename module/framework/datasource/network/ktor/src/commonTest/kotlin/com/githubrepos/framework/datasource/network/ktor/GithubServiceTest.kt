package com.githubrepos.framework.datasource.network.ktor

import com.githubrepos.adapter.data.datasource.network.NetworkServiceProvider
import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.adapter.data.test.repoCountInJSON
import com.githubrepos.core.entity.toListRepo
import com.githubrepos.core.test.repo0
import com.githubrepos.core.test.repo1
import com.githubrepos.framework.datasource.network.ktor.di.createNetworkServiceProvider
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GithubServiceTest {

    private lateinit var networkServiceProvider: NetworkServiceProvider
    private lateinit var githubService: GithubService
    private lateinit var engineProvider: GithubServiceEngineManager

    @BeforeTest
    fun setUp() {
        engineProvider = GithubServiceEngineManager()
        networkServiceProvider = createNetworkServiceProvider(true, engine = engineProvider.mockEngine)
        githubService = networkServiceProvider.createGithubService()
    }

    @Test
    fun getRepo() = runTest {
        val repo = githubService.getRepo(repo0.id)
        assertEquals(repo0, repo)

        engineProvider.hasNetworkConnection = false
        assertFailsWith<NoInternetException> {
            githubService.getRepo(repo0.id)
        }
    }

    @Test
    fun searchRepos() = runTest {
        val response = githubService.searchRepos("", 1, 2)
        assertEquals(repoCountInJSON, response.total)
        assertEquals(2, response.items.size)
        assertEquals(repo0.toListRepo(), response.items[0])
        assertEquals(repo1.toListRepo(), response.items[1])

        engineProvider.hasNetworkConnection = false
        assertFailsWith<NoInternetException> {
            githubService.searchRepos("", 1, 2)
        }
    }
}