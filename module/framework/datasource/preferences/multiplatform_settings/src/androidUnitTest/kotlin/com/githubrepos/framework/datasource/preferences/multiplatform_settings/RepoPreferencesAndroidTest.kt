package com.githubrepos.framework.datasource.preferences.multiplatform_settings

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.di.createRepoPreferences
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


internal fun createTestRepoPreferences(): RepoPreferences {
    val context = ApplicationProvider.getApplicationContext<Context>()
    return createRepoPreferences(context, TEST_REPO_PREFERENCES_NAME)
}

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner::class)
internal class RepoPreferencesAndroidTest: RepoPreferencesTest(createTestRepoPreferences()) {

    @Test
    override fun readAndWriteTest() {
        super.readAndWriteTest()
    }
}