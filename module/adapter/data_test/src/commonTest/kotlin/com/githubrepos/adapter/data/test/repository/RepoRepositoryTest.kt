package com.githubrepos.adapter.data.test.repository

import app.cash.turbine.test
import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.di.createRepoRepository
import com.githubrepos.adapter.data.test.datasource.database.dao.FakeRepoDao
import com.githubrepos.adapter.data.test.datasource.network.FakeJsonNetworkServiceProvider
import com.githubrepos.adapter.data.test.datasource.network.FakeNetworkStateProvider
import com.githubrepos.core.Lce
import com.githubrepos.core.test.repo1
import com.githubrepos.core.test.repo2
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class RepoRepositoryTest {

    private val networkStateProvider = FakeNetworkStateProvider()
    private val networkServiceProvider = FakeJsonNetworkServiceProvider(networkStateProvider)
    private val githubService = networkServiceProvider.createGithubService()
    private val repoDao = FakeRepoDao()
    private val repoRepository = createRepoRepository(networkStateProvider, githubService, repoDao)

    @Test
    fun `getRepo with empty database`() = runTest {
        repoRepository.getRepo(repo1.id).test {
            assertEquals(Lce.Loading, awaitItem())
            assertEquals(Lce.Content(repo1), awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `getRepo with old data in database`() = runTest {
        val oldRepo0 = repo2.copy(id = repo1.id)
        repoDao.insert(oldRepo0)

        repoRepository.getRepo(repo1.id).test {
            assertEquals(Lce.Content(oldRepo0), awaitItem())
            assertEquals(Lce.Loading, awaitItem())
            assertEquals(Lce.Content(repo1), awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `getRepo freshDataOnly`() = runTest {
        val oldRepo0 = repo2.copy(id = repo1.id)
        repoDao.insert(oldRepo0)

        repoRepository.getRepo(repo1.id, true).test {
            assertEquals(Lce.Loading, awaitItem())
            assertEquals(Lce.Content(repo1), awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `getRepo - no internet`() = runTest {
        networkStateProvider.hasNetworkConnection = false

        repoRepository.getRepo(repo1.id).test {
            assertEquals(Lce.Loading, awaitItem())
            val item = awaitItem()
            assertIs<Lce.Error>(item)
            assertIs<NoInternetException>(item.error)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `getRepo - no internet - with old data in database`() = runTest {
        networkStateProvider.hasNetworkConnection = false
        val oldRepo0 = repo2.copy(id = repo1.id)
        repoDao.insert(oldRepo0)

        repoRepository.getRepo(repo1.id).test {
            assertEquals(Lce.Content(oldRepo0), awaitItem())
            ensureAllEventsConsumed()
        }
    }
}