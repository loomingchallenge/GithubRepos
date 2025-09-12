package com.githubrepos.adapter.presentation.viewmodel

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.test.datasource.network.FakeNetworkStateProvider
import com.githubrepos.adapter.presentation.di.createRepoViewModel
import com.githubrepos.core.test.repo0
import com.githubrepos.core.test.repo0New
import com.githubrepos.core.test.repo0Old
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class RepoViewModelTest {

    private val networkStateProvider = FakeNetworkStateProvider()
    private val getRepoUseCase = FakeGetRepoUseCase(networkStateProvider)
    private lateinit var repoViewModel: RepoViewModel

    @Test
    fun init() = runTest {
        repoViewModel = createRepoViewModel( this)
        assertEquals("Repo", repoViewModel.title)
        createRepoViewModel( this).repoLceFlows.content.test {
            assertEquals(null, awaitItem())
            assertEquals(repo0Old, awaitItem())
            assertEquals(repo0, awaitItem())
            ensureAllEventsConsumed()
        }
        createRepoViewModel( this).repoLceFlows.loading.test {
            assertEquals(false, awaitItem())
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
            ensureAllEventsConsumed()
        }
        createRepoViewModel( this).repoLceFlows.error.test {
            assertEquals(null, awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `loadRepo - internet`() = runTest {
        repoViewModel = createRepoViewModel( this)
        yield()
        turbineScope {
            val loading = repoViewModel.repoLceFlows.loading.testIn(backgroundScope)
            val content = repoViewModel.repoLceFlows.content.testIn(backgroundScope)
            val error = repoViewModel.repoLceFlows.error.testIn(backgroundScope)
            assertEquals(false, loading.awaitItem())
            assertEquals(repo0, content.awaitItem())
            assertEquals(null, error.awaitItem())
            repoViewModel.loadRepo(false)
            assertEquals(repo0Old, content.awaitItem())
            assertEquals(true, loading.awaitItem())
            assertEquals(repo0, content.awaitItem())
            assertEquals(false, loading.awaitItem())
            repoViewModel.loadRepo(true)
            assertEquals(true, loading.awaitItem())
            assertEquals(repo0New, content.awaitItem())
            assertEquals(false, loading.awaitItem())
        }
    }

    @Test
    fun `loadRepo - no internet`() = runTest {
        networkStateProvider.hasNetworkConnection = false
        turbineScope {
            repoViewModel = createRepoViewModel( this)
            val loading = repoViewModel.repoLceFlows.loading.testIn(backgroundScope)
            val content = repoViewModel.repoLceFlows.content.testIn(backgroundScope)
            val error = repoViewModel.repoLceFlows.error.testIn(backgroundScope)
            assertEquals(false, loading.awaitItem())
            assertEquals(null, content.awaitItem())
            assertEquals(null, error.awaitItem())
            assertEquals(repo0Old, content.awaitItem())
            repoViewModel.loadRepo(true)
            assertEquals(true, loading.awaitItem())
            assertIs<NoInternetException>(error.awaitItem())
            assertEquals(false, loading.awaitItem())
        }
    }

    private fun createRepoViewModel(scope: CoroutineScope): RepoViewModel {
        return createRepoViewModel(repo0.id, "Repo", scope, getRepoUseCase)
    }
}