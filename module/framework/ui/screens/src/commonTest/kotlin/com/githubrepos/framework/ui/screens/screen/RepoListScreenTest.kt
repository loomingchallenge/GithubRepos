package com.githubrepos.framework.ui.screens.screen

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeDown
import androidx.paging.LoadState
import com.githubrepos.adapter.data.test.datasource.network.FakeNetworkStateProvider
import com.githubrepos.adapter.data.test.datasource.network.service.FakeGithubService
import com.githubrepos.core.test.generateListRepos
import com.githubrepos.framework.ui.screens.theme.MainTheme
import com.githubrepos.framework.ui.screens.viewmodel.FakeRepoListViewModel
import com.githubrepos.framework.ui.test.UITest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull

class RepoListScreenTest: UITest() {

    companion object {
        private val networkStateProvider = FakeNetworkStateProvider()
        private val githubService = FakeGithubService(networkStateProvider, generateListRepos(50))

        private val listRepos = runBlocking {
            githubService.searchRepos("", 1, 10).items
        }
    }

    private lateinit var viewModel: FakeRepoListViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    override fun setUp() {
        super.setUp()
        viewModel = FakeRepoListViewModel(true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    override fun tearDown() {
        super.tearDown()
    }

    @OptIn(ExperimentalTestApi::class, ExperimentalCoroutinesApi::class)
    @Test
    fun testLoading() = runComposeUiTest {
        setContent(this)

        viewModel.emitListRepo(refresh = LoadState.Loading)
        waitForIdle()
        onNodeWithTag(RepoListScreenTags.PROGRESS_INDICATOR).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.RETRY_VIEW).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST).assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testContent() = runComposeUiTest {
        setContent(this)

        viewModel.emitListRepo(listRepos)
        waitForIdle()
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.RETRY_VIEW).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.PROGRESS_INDICATOR).assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testError() = runComposeUiTest {
        setContent(this)

        viewModel.emitListRepo(refresh = LoadState.Error(Exception("Error")))
        waitForIdle()
        onNodeWithTag(RepoListScreenTags.RETRY_VIEW).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST).assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testContentWithPrependLoading() = runComposeUiTest {
        setContent(this)

        viewModel.emitListRepo(listRepos, prepend = LoadState.Loading)
        waitForIdle()
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.PREPEND_LOADING).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.PREPEND_ERROR).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.APPEND_LOADING).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.APPEND_ERROR).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.RETRY_VIEW).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.PROGRESS_INDICATOR).assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testContentWithPrependError() = runComposeUiTest {
        setContent(this)

        viewModel.emitListRepo(listRepos, prepend = LoadState.Error(Exception("Error")))
        waitForIdle()
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.PREPEND_ERROR).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.PREPEND_LOADING).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.APPEND_LOADING).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.APPEND_ERROR).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.RETRY_VIEW).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.PROGRESS_INDICATOR).assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testContentWithAppendLoading() = runComposeUiTest {
        setContent(this)

        viewModel.emitListRepo(listRepos, append = LoadState.Loading)
        waitForIdle()
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.RETRY_VIEW).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.PREPEND_ERROR).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.PREPEND_LOADING).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST)
            .performScrollToNode(hasTestTag(RepoListScreenTags.APPEND_LOADING))
        onNodeWithTag(RepoListScreenTags.APPEND_LOADING).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.APPEND_ERROR).assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testContentWithAppendError() = runComposeUiTest {
        setContent(this)

        viewModel.emitListRepo(listRepos, append = LoadState.Error(Exception("Error")))
        waitForIdle()
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.RETRY_VIEW).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.PREPEND_ERROR).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.PREPEND_LOADING).assertDoesNotExist()
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST)
            .performScrollToNode(hasTestTag(RepoListScreenTags.APPEND_ERROR))
        onNodeWithTag(RepoListScreenTags.APPEND_ERROR).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.APPEND_LOADING).assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testPullToRefresh() = runComposeUiTest {
        setContent(this)

        viewModel.emitListRepo(listRepos)
        waitForIdle()
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST).assertIsDisplayed()
        viewModel.setRepositoryError(Exception())
        onNodeWithTag(RepoListScreenTags.SCREEN).performTouchInput {
            swipeDown()
        }
        assertNull(viewModel.repositoryError)
        onNodeWithTag(RepoListScreenTags.PULL_REFRESH).assertExists()
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    private fun setContent(composeUiTest: ComposeUiTest) {
        composeUiTest.setContent {
            MainTheme(darkTheme = false) {
                RepoListScreen(viewModel, {}, {})
            }
        }
    }
}