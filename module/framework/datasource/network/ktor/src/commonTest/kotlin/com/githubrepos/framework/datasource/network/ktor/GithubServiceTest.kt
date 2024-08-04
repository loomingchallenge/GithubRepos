package com.githubrepos.framework.datasource.network.ktor

import com.githubrepos.adapter.data.datasource.network.NetworkServiceProvider
import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.entity.toListRepo
import com.githubrepos.framework.datasource.network.ktor.di.createNetworkServiceProvider
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GithubServiceTest {

    private lateinit var networkServiceProvider: NetworkServiceProvider
    private lateinit var githubService: GithubService
    private lateinit var engineProvider: GithubServiceEngineProvider

    @BeforeTest
    fun setUp() {
        engineProvider = GithubServiceEngineProvider()
        networkServiceProvider = createNetworkServiceProvider(true, engine = engineProvider.mockEngine)
        githubService = networkServiceProvider.createGithubService()
    }

    @Test
    fun getRepo() = runTest {
        val repo = githubService.getRepo(expRepo0.id)
        assertEquals(expRepo0, repo)

        engineProvider.hasNetworkConnection = false
        assertFailsWith<NoInternetException> {
            githubService.getRepo(expRepo0.id)
        }
    }

    @Test
    fun searchRepos() = runTest {
        val response = githubService.searchRepos("", 1, 2)
        assertEquals(totalCount, response.total)
        assertEquals(2, response.items.size)
        assertEquals(expRepo0.toListRepo(), response.items[0])
        assertEquals(expRepo1.toListRepo(), response.items[1])

        engineProvider.hasNetworkConnection = false
        assertFailsWith<NoInternetException> {
            githubService.searchRepos("", 1, 2)
        }
    }

    private val totalCount = 50

    private val expRepo0 = Repo(
        31792824,
        "flutter",
        "flutter/flutter",
        "Flutter makes it easy and fast to build beautiful apps for mobile and beyond",
        "https://github.com/flutter/flutter",
        "https://flutter.dev",
        "https://github.com/flutter/flutter.git",
        "https://api.github.com/repos/flutter/flutter/deployments",
        159256,
        26722,
        "Dart"
    )

    private val expRepo1 = Repo(
        29028775,
        "react-native",
        "facebook/react-native",
        "A framework for building native applications using React",
        "https://github.com/facebook/react-native",
        "https://reactnative.dev",
        "https://github.com/facebook/react-native.git",
        "https://api.github.com/repos/facebook/react-native/deployments",
        113772,
        24144,
        "C++"
    )
}