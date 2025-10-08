package com.githubrepos.framework.datasource.preferences.multiplatform_settings

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.preferences.RepoPreferencesImp
import com.russhwolf.settings.SharedPreferencesSettings
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

internal fun createTestRepoPreferences(): RepoPreferences {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val sharedPreferences = context.getSharedPreferences(TEST_REPO_PREFERENCES_NAME, Context.MODE_PRIVATE)
    return RepoPreferencesImp(SharedPreferencesSettings(sharedPreferences))
}

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner::class)
internal class RepoPreferencesAndroidTest: RepoPreferencesTest(createTestRepoPreferences())