package com.githubrepos.di

import android.content.Context
import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.di.createRepoPreferences
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class SettingsModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            singleOf<RepoPreferences, Context>(::createRepoPreferences)
        }
    }
}