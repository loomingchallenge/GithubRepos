package com.githubrepos.framework.ui.screens.screen

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeDown
import com.githubrepos.adapter.presentation.di.createErrorReporter
import com.githubrepos.adapter.presentation.error.ErrorReporter
import com.githubrepos.core.test.repo0
import com.githubrepos.framework.ui.screens.components.LoadingTestTags
import com.githubrepos.framework.ui.screens.theme.MainTheme
import com.githubrepos.framework.ui.screens.viewmodel.FakeRepoViewModel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RepoScreenTest {

    private lateinit var viewModel: FakeRepoViewModel
    private lateinit var errorReporter: ErrorReporter

    @BeforeTest
    fun setUp() {
        viewModel = FakeRepoViewModel()
        errorReporter = createErrorReporter()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testLoadingContentError() = runComposeUiTest {
        setContent(this)

        viewModel.emit(loading = true)
        onNodeWithTag(LoadingTestTags.PROGRESS_INDICATOR).assertExists()
        onNodeWithTag(LoadingTestTags.SMALL_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoScreenTags.REPO).assertDoesNotExist()
        onNodeWithTag(LoadingTestTags.RETRY_BUTTON).assertDoesNotExist()
        assertNull(errorReporter.error.value)

        viewModel.emit(content = repo0)
        onNodeWithTag(LoadingTestTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(LoadingTestTags.SMALL_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(LoadingTestTags.RETRY_BUTTON).assertDoesNotExist()
        assertNull(errorReporter.error.value)
        onNodeWithTag(RepoScreenTags.REPO).assertExists()
        onNodeWithTag(RepoScreenTags.REPO_FULL_NAME).assertTextEquals(repo0.fullName)
        onNodeWithTag(RepoScreenTags.REPO_DESCRIPTION).assertTextEquals(repo0.description ?: "")
        onNodeWithTag(RepoScreenTags.REPO_URL).assertTextEquals(repo0.url ?: "")
        onNodeWithTag(RepoScreenTags.REPO_HOMEPAGE).assertTextEquals(repo0.homepage ?: "")
        onNodeWithTag(RepoScreenTags.REPO_CLONE_URL).assertTextEquals(repo0.cloneUrl ?: "")
        onNodeWithTag(RepoScreenTags.REPO_DEPLOYMENTS_URL).assertTextEquals(repo0.deploymentsUrl ?: "")
        onNodeWithTag(RepoScreenTags.REPO_LANGUAGE).assertTextContains( repo0.language ?: "", substring = true)
        onNodeWithTag(RepoScreenTags.REPO_STARS).assertTextEquals(repo0.stars.toString())
        onNodeWithTag(RepoScreenTags.REPO_FORKS).assertTextEquals(repo0.forks.toString())

        val exc = Exception("Error")
        viewModel.emit(error = exc)
        onNodeWithTag(LoadingTestTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(LoadingTestTags.SMALL_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(RepoScreenTags.REPO).assertDoesNotExist()
        assertNull(errorReporter.error.value)
        onNodeWithTag(LoadingTestTags.RETRY_BUTTON).assertExists()
        onNodeWithTag(LoadingTestTags.RETRY_BUTTON_TEXT).assertTextEquals(exc.message!!)

        viewModel.emit(loading = true, content = repo0)
        onNodeWithTag(RepoScreenTags.REPO).assertExists()
        onNodeWithTag(LoadingTestTags.SMALL_PROGRESS_INDICATOR).assertExists()
        onNodeWithTag(LoadingTestTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(LoadingTestTags.RETRY_BUTTON).assertDoesNotExist()
        assertNull(errorReporter.error.value)

        errorReporter.clearError()
        viewModel.emit(content = repo0, error = exc)
        onNodeWithTag(RepoScreenTags.REPO).assertExists()
        onNodeWithTag(LoadingTestTags.SMALL_PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(LoadingTestTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(LoadingTestTags.RETRY_BUTTON).assertDoesNotExist()
        assertEquals(exc, errorReporter.error.value!!.exc)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testPullToRefresh() = runComposeUiTest {
        setContent(this)

        viewModel.emit(content = repo0)
        onNodeWithTag(RepoScreenTags.PULL_REFRESH).assertExists()
        assertFalse(viewModel.wasLoadRepoCalled)
        assertNull(viewModel.freshDataOnly)
        onNodeWithTag(RepoScreenTags.SCREEN).performTouchInput {
            swipeDown()
        }
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