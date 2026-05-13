package com.githubrepos.di

import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import org.koin.dsl.module
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.di.createRepoPreferences
import org.koin.core.module.Module

class SettingsModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            single<RepoPreferences> { createRepoPreferences() }
        }
    }
}