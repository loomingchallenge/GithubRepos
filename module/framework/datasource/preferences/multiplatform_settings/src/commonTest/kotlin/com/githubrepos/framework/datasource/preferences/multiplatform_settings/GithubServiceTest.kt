package com.githubrepos.framework.datasource.preferences.multiplatform_settings

import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.preferences.REPO_PREFERENCES_NAME
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal const val TEST_REPO_PREFERENCES_NAME = "TEST$REPO_PREFERENCES_NAME"

@Ignore
internal open class RepoPreferencesTest(private val repoPreferences: RepoPreferences) {

    @Test
    open fun readAndWriteTest() = runTest {
        repoPreferences.clean()
        try {
            assertNull(repoPreferences.query)
            repoPreferences.query = "example"
            assertEquals("example", repoPreferences.query)

            repoPreferences.clean()
            assertNull(repoPreferences.query)
        } finally {
            repoPreferences.clean()
        }
    }
}