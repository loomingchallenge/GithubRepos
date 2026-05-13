package com.githubrepos.framework.datasource.preferences.multiplatform_settings.di

import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.preferences.RepoPreferencesImp
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.StorageSettings

@OptIn(ExperimentalSettingsImplementation::class)
fun createRepoPreferences(): RepoPreferences {
    return RepoPreferencesImp(StorageSettings()) // non encrypted storage
}

