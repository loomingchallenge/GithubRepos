package com.githubrepos.framework.ui.screens.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeDown
import androidx.paging.LoadState
import com.githubrepos.adapter.data.test.datasource.network.FakeNetworkStateProvider
import com.githubrepos.adapter.data.test.datasource.network.service.FakeGithubService
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.core.test.generateListRepos
import com.githubrepos.framework.ui.screens.theme.MainTheme
import com.githubrepos.framework.ui.screens.viewmodel.FakeRepoListViewModel
import com.githubrepos.framework.ui.test.UITest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RepoListScreenTest: UITest() {

    companion object {
        private val networkStateProvider = FakeNetworkStateProvider()
        private val githubService = FakeGithubService(networkStateProvider, generateListRepos(50))

        private var listRepos: List<ListRepo>? = null

        private suspend fun getListRepos(): List<ListRepo> {
            return listRepos ?: githubService.searchRepos("", 1, 10).items.also {
                listRepos = it
            }
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

        viewModel.emitListRepo(getListRepos())
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

        viewModel.emitListRepo(getListRepos(), prepend = LoadState.Loading)
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

        viewModel.emitListRepo(getListRepos(), prepend = LoadState.Error(Exception("Error")))
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

        viewModel.emitListRepo(getListRepos(), append = LoadState.Loading)
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

        viewModel.emitListRepo(getListRepos(), append = LoadState.Error(Exception("Error")))
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

        viewModel.emitListRepo(getListRepos())
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
    @Test
    fun testSearchFlow() = runComposeUiTest {
        setContent(this)

        // Initially search bar should not exist
        onNodeWithTag(RepoListScreenTags.SEARCH_BAR).assertDoesNotExist()

        // Click search button
        onNodeWithTag(RepoListScreenTags.SEARCH_BUTTON).assertIsDisplayed().performClick()

        // Search bar should appear
        onNodeWithTag(RepoListScreenTags.SEARCH_BAR).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.SEARCH_BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.SEARCH_TEXT_FIELD).assertIsDisplayed()

        // Type search query
        onNodeWithTag(RepoListScreenTags.SEARCH_TEXT_FIELD).performTextInput("Kotlin")
        assertEquals("Kotlin", viewModel.searchQuery.value)

        // Clear button should appear and clear text
        onNodeWithTag(RepoListScreenTags.SEARCH_CLEAR_BUTTON).assertIsDisplayed().performClick()
        assertEquals("", viewModel.searchQuery.value)

        // Click back button to exit search
        onNodeWithTag(RepoListScreenTags.SEARCH_BACK_BUTTON).performClick()
        onNodeWithTag(RepoListScreenTags.SEARCH_BAR).assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    private fun setContent(composeUiTest: ComposeUiTest) {
        composeUiTest.setContent {
            MainTheme(darkTheme = false) {
                val topBar: MutableState<@Composable () -> Unit> =  remember { mutableStateOf(@Composable { }) }
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .windowInsetsPadding(WindowInsets.displayCutout)
                ) {
                    Scaffold(
                        modifier = Modifier.windowInsetsPadding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                        ),
                        topBar = topBar.value
                    ) { innerPadding ->
                        RepoListScreen(viewModel, { topBar.value = it }, {})
                    }
                }
            }
        }
    }
}