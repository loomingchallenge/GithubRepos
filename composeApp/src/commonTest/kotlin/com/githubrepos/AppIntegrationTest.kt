package com.githubrepos

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeDown
import com.githubrepos.adapter.data.datasource.database.RepoDatabase
import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import com.githubrepos.adapter.data.test.datasource.network.FakeNetworkStateProvider
import com.githubrepos.core.test.repo1
import com.githubrepos.core.test.repo20
import com.githubrepos.di.KoinHelper.getKoin
import com.githubrepos.di.KoinManager
import com.githubrepos.framework.ui.screens.components.LoadingTestTags
import com.githubrepos.framework.ui.screens.screen.RepoListScreenTags
import com.githubrepos.framework.ui.screens.screen.RepoScreenTags
import com.githubrepos.framework.ui.test.UITest
import com.githubrepos.framework.ui.test.waitUntilDisplayed
import com.githubrepos.ui.UI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

abstract class AppIntegrationTest: UITest() {

    abstract val koinManager: KoinManager

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    override fun setUp() {
        super.setUp()
        koinManager.startKoin()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    override fun tearDown() {
        getKoin().get<RepoDatabase>().close()
        koinManager.stopKoin()
        super.tearDown()
    }

    @OptIn(ExperimentalTestApi::class, ExperimentalCoroutinesApi::class)
    @Test
    open fun testNavigation() = runComposeUiTest {
        setContent(this)

        waitUntilDisplayed(RepoListScreenTags.SCREEN)
        waitUntilDisplayed(RepoListScreenTags.REPOSITORY_LIST)
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST).performScrollToIndex(20)
        onNodeWithTag(RepoListScreenTags.getRepositoryListItem(repo20.id))
            .assertIsDisplayed()
            .performClick()
        waitUntilDisplayed(RepoScreenTags.SCREEN)
        waitUntilDisplayed(RepoScreenTags.REPO)
        onNodeWithTag(RepoScreenTags.BACK_BUTTON)
            .assertIsDisplayed()
            .performClick()
        waitUntilDisplayed(RepoListScreenTags.SCREEN)
        waitUntilDisplayed(RepoListScreenTags.REPOSITORY_LIST)
        onNodeWithTag(RepoListScreenTags.getRepositoryListItem(repo20.id))
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    open fun testRetry() = runComposeUiTest {
        val networkStateProvider = getKoin().get<NetworkStateProvider>() as FakeNetworkStateProvider
        networkStateProvider.hasNetworkConnection = false

        setContent(this)

        waitUntilDisplayed(RepoListScreenTags.SCREEN)
        waitUntilDisplayed(RepoListScreenTags.RETRY_VIEW)
        networkStateProvider.hasNetworkConnection = true
        onNodeWithTag(LoadingTestTags.getRetryViewButtonTag(RepoListScreenTags.RETRY_VIEW))
            .performClick()
        waitUntilDisplayed(RepoListScreenTags.SCREEN)
        waitUntilDisplayed(RepoListScreenTags.REPOSITORY_LIST)
        onNodeWithTag(RepoListScreenTags.getRepositoryListItem(repo1.id)).assertIsDisplayed()
        networkStateProvider.hasNetworkConnection = false
        onNodeWithTag(RepoListScreenTags.getRepositoryListItem(repo1.id)).performClick()
        waitUntilDisplayed(RepoScreenTags.SCREEN)
        waitUntilDisplayed(RepoScreenTags.RETRY_VIEW)
        networkStateProvider.hasNetworkConnection = true
        onNodeWithTag(LoadingTestTags.getRetryViewButtonTag(RepoScreenTags.RETRY_VIEW))
            .performClick()
        waitUntilDisplayed(RepoScreenTags.SCREEN)
        waitUntilDisplayed(RepoScreenTags.REPO)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    open fun testRefresh() = runComposeUiTest {
        setContent(this)

        waitUntilDisplayed(RepoListScreenTags.SCREEN)
        waitUntilDisplayed(RepoListScreenTags.REPOSITORY_LIST)
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST).performScrollToIndex(20)
        onNodeWithTag(RepoListScreenTags.getRepositoryListItem(repo20.id)).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.OVERFLOW_MENU)
            .assertIsDisplayed()
            .performClick()
        waitUntilDisplayed(RepoListScreenTags.REFRESH_BUTTON)
        onNodeWithTag(RepoListScreenTags.REFRESH_BUTTON)
            .performClick()
        waitUntilDisplayed(RepoListScreenTags.REPOSITORY_LIST)
        onNodeWithTag(RepoListScreenTags.getRepositoryListItem(repo20.id)).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    open fun testPullRefresh() = runComposeUiTest {
        setContent(this)

        waitUntilDisplayed(RepoListScreenTags.SCREEN)
        waitUntilDisplayed(RepoListScreenTags.REPOSITORY_LIST)
        onNodeWithTag(RepoListScreenTags.getRepositoryListItem(repo1.id)).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.SCREEN).performTouchInput {
            swipeDown()
        }
        onNodeWithTag(RepoListScreenTags.PULL_REFRESH).assertExists()
        onNodeWithTag(RepoListScreenTags.REPOSITORY_LIST).assertIsDisplayed()
        onNodeWithTag(RepoListScreenTags.getRepositoryListItem(repo1.id)).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    private fun setContent(composeUiTest: ComposeUiTest) {
        composeUiTest.setContent {
            UI()
        }
    }
}