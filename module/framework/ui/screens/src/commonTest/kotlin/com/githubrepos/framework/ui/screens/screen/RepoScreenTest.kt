package com.githubrepos.framework.ui.screens.screen

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeDown
import com.githubrepos.adapter.presentation.di.createErrorReporter
import com.githubrepos.adapter.presentation.error.ErrorReporter
import com.githubrepos.core.test.repo1
import com.githubrepos.framework.ui.screens.components.LoadingTestTags
import com.githubrepos.framework.ui.screens.theme.MainTheme
import com.githubrepos.framework.ui.screens.viewmodel.FakeRepoViewModel
import com.githubrepos.framework.ui.test.UITest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RepoScreenTest: UITest() {

    private lateinit var viewModel: FakeRepoViewModel
    private lateinit var errorReporter: ErrorReporter

    @BeforeTest
    override fun setUp() {
        super.setUp()
        viewModel = FakeRepoViewModel()
        errorReporter = createErrorReporter()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    override fun tearDown() {
        super.tearDown()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testLoading() = runComposeUiTest {
        setContent(this)

        viewModel.emit(loading = true)
        onNodeWithTag(RepoScreenTags.PROGRESS_INDICATOR).assertExists()
        onNodeWithTag(RepoScreenTags.SMALL_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoScreenTags.REPO).assertDoesNotExist()
        onNodeWithTag(RepoScreenTags.RETRY_VIEW).assertDoesNotExist()
        assertNull(errorReporter.error.value)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testContent() = runComposeUiTest {
        setContent(this)

        viewModel.emit(content = repo1)
        onNodeWithTag(RepoScreenTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoScreenTags.SMALL_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoScreenTags.RETRY_VIEW).assertDoesNotExist()
        assertNull(errorReporter.error.value)
        onNodeWithTag(RepoScreenTags.REPO).assertExists()
        onNodeWithTag(RepoScreenTags.REPO_FULL_NAME).assertTextEquals(repo1.fullName)
        onNodeWithTag(RepoScreenTags.REPO_DESCRIPTION).assertTextEquals(repo1.description ?: "")
        onNodeWithTag(RepoScreenTags.REPO_URL).assertTextEquals(repo1.url ?: "")
        onNodeWithTag(RepoScreenTags.REPO_HOMEPAGE).assertTextEquals(repo1.homepage ?: "")
        onNodeWithTag(RepoScreenTags.REPO_CLONE_URL).assertTextEquals(repo1.cloneUrl ?: "")
        onNodeWithTag(RepoScreenTags.REPO_DEPLOYMENTS_URL).assertTextEquals(repo1.deploymentsUrl ?: "")
        onNodeWithTag(RepoScreenTags.REPO_LANGUAGE).assertTextContains( repo1.language ?: "", substring = true)
        onNodeWithTag(RepoScreenTags.REPO_STARS).assertTextEquals(repo1.stars.toString())
        onNodeWithTag(RepoScreenTags.REPO_FORKS).assertTextEquals(repo1.forks.toString())
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testError() = runComposeUiTest {
        setContent(this)

        val exc = Exception("Error")
        viewModel.emit(error = exc)
        onNodeWithTag(RepoScreenTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoScreenTags.SMALL_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoScreenTags.REPO).assertDoesNotExist()
        assertNull(errorReporter.error.value)
        onNodeWithTag(RepoScreenTags.RETRY_VIEW).assertExists()
        onNodeWithTag(LoadingTestTags.getRetryViewTextTag(RepoScreenTags.RETRY_VIEW))
            .assertTextEquals(exc.message!!)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testContentWithLoading() = runComposeUiTest {
        setContent(this)

        viewModel.emit(loading = true, content = repo1)
        onNodeWithTag(RepoScreenTags.REPO).assertExists()
        onNodeWithTag(RepoScreenTags.SMALL_PROGRESS_INDICATOR).assertExists()
        onNodeWithTag(RepoScreenTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoScreenTags.RETRY_VIEW).assertDoesNotExist()
        assertNull(errorReporter.error.value)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testContentWithError() = runComposeUiTest {
        setContent(this)
        val exc = Exception("Error")
        errorReporter.clearError()
        viewModel.emit(content = repo1, error = exc)
        onNodeWithTag(RepoScreenTags.REPO).assertExists()
        onNodeWithTag(RepoScreenTags.SMALL_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoScreenTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoScreenTags.RETRY_VIEW).assertDoesNotExist()
        assertEquals(exc, errorReporter.error.value!!.exc)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testPullToRefresh() = runComposeUiTest {
        setContent(this)

        viewModel.emit(content = repo1)
        onNodeWithTag(RepoScreenTags.PULL_REFRESH).assertExists()
        assertFalse(viewModel.wasLoadRepoCalled)
        assertNull(viewModel.freshDataOnly)
        onNodeWithTag(RepoScreenTags.SCREEN).performTouchInput {
            swipeDown(startY = 50.0f)
        }
        assertTrue(viewModel.wasLoadRepoCalled)
        assertTrue(viewModel.freshDataOnly!!)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testRetryButton() = runComposeUiTest {
        setContent(this)

        val exc = Exception("Error")
        viewModel.emit(error = exc)
        onNodeWithTag(RepoScreenTags.RETRY_VIEW).assertExists()
        assertFalse(viewModel.wasLoadRepoCalled)
        assertNull(viewModel.freshDataOnly)
        onNodeWithTag(LoadingTestTags.getRetryViewButtonTag(RepoScreenTags.RETRY_VIEW))
            .performClick()
        assertTrue(viewModel.wasLoadRepoCalled)
        assertTrue(viewModel.freshDataOnly!!)
    }

    @OptIn(ExperimentalTestApi::class)
    private fun setContent(composeUiTest: ComposeUiTest) {
        composeUiTest.setContent {
            MainTheme(darkTheme = false) {
                RepoScreen(viewModel, errorReporter, {}, {})
            }
        }
    }
}