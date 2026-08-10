package com.githubrepos.di

import com.githubrepos.AndroidConfig

fun getAndroidModuleFactories(isDebug: Boolean): List<ModuleFactory> {
    return mutableListOf(
        MainModuleFactory(),
        SettingsModuleFactory(),
        AndroidSQLModuleFactory(),
        AndroidCoreModuleFactory()
    ).apply {
        addAll(getCommonModuleFactories(AndroidConfig(isDebug)))
    }
}