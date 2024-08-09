package com.githubrepos.di

import android.content.Context
import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.di.createRepoPreferences

val settingsModule = module {
    singleOf<RepoPreferences, Context>(::createRepoPreferences)
}
