package com.githubrepos.di

import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.russhwolf.settings.MapSettings
import org.koin.core.module.Module
import org.koin.dsl.module
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.di.createRepoPreferences

class TestSettingsModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            single<RepoPreferences> { createRepoPreferences(MapSettings()) }
        }
    }
}