package com.githubrepos.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.di.createRepoPreferences

val settingsModule = module {
    singleOf(::createRepoPreferences)
}
