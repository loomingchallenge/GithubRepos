package com.githubrepos.framework.ui.test

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.waitUntilCreated(
    tag: String,
    timeoutMillis: Long = 1000
) {
    waitUntil(timeoutMillis = timeoutMillis) {
        onAllNodesWithTag(tag)
            .fetchSemanticsNodes()
            .isNotEmpty()
    }
}

@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.waitUntilDisplayed(
    tag: String,
    timeoutMillis: Long = 1000
) {
    waitUntilCreated(tag, timeoutMillis)
    onNodeWithTag(tag).assertIsDisplayed()
}

@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.waitUntilDisplayedAndClick(
    tag: String,
    timeoutMillis: Long = 1000
) {
    waitUntilDisplayed(tag, timeoutMillis)
    onNodeWithTag(tag)
        .assertIsDisplayed()
        .performClick()
}