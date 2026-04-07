package com.githubrepos.framework.datasource.preferences.multiplatform_settings.di

import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.preferences.RepoPreferencesImp
import com.russhwolf.settings.Settings

fun createRepoPreferences(settings: Settings): RepoPreferences {

    return RepoPreferencesImp(settings)
}

