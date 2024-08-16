package com.githubrepos.framework.datasource.preferences.multiplatform_settings

import com.githubrepos.framework.datasource.preferences.multiplatform_settings.di.createRepoPreferences
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.id.test.repo.TestRepoPreferencesID

internal class RepoPreferencesIOSTest: RepoPreferencesTest(createRepoPreferences(TestRepoPreferencesID::class.java))