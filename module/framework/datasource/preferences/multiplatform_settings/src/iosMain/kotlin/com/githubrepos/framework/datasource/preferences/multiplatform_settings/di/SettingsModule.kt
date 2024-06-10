package com.githubrepos.framework.datasource.preferences.multiplatform_settings.di

import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.preferences.RepoPreferencesImp
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.preferences.repoPreferencesName
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings

@OptIn(ExperimentalSettingsImplementation::class)
fun createRepoPreferences(): RepoPreferences {
    return RepoPreferencesImp(KeychainSettings(repoPreferencesName))
}

