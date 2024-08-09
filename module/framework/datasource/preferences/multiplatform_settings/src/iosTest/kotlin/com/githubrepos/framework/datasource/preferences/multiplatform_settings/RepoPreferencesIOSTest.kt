package com.githubrepos.framework.datasource.preferences.multiplatform_settings

import com.githubrepos.framework.datasource.preferences.multiplatform_settings.di.createRepoPreferences
import kotlin.test.Test

internal class RepoPreferencesIOSTest: RepoPreferencesTest(createRepoPreferences(TEST_REPO_PREFERENCES_NAME)) {

    @Test
    override fun readAndWriteTest() {
        super.readAndWriteTest()
    }
}