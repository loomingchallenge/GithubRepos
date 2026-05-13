package com.githubrepos.di

import com.githubrepos.ConfigImp

fun getAndroidModuleFactories(): List<ModuleFactory> {
    return mutableListOf(
        MainModuleFactory(),
        SettingsModuleFactory(),
        AndroidSQLModuleFactory(),
        AndroidCoreModuleFactory()
    ).apply {
        addAll(getCommonModuleFactories(ConfigImp))
    }
}