package com.githubrepos.adapter.data

import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.framework.datasource.network.demo_api.di.createGithubService
import com.githubrepos.framework.datasource.network.demo_api.di.createNetworkServiceProvider
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class RepoRepositoryTest {

    private val networkStateProvider = NetworkStateProviderImp()
    private val networkServiceProvider = createNetworkServiceProvider(networkStateProvider)
    private val githubService = createGithubService(networkServiceProvider)

    @Test
    fun getRepo() = runTest {
        networkStateProvider.hasNetworkConnection = true

        var repo = githubService.getRepo(31792824)
        assertEquals(31792824, repo.id)

        assertFailsWith<IllegalArgumentException> {
            githubService.getRepo(-1)
        }
    }
}

class NetworkStateProviderImp : NetworkStateProvider {

    var hasNetworkConnection = true

    override fun hasNetworkConnection(): Boolean {
        return hasNetworkConnection
    }
}