package com.githubrepos.di

import com.githubrepos.ConfigImp

fun getJvmModuleFactories(): List<ModuleFactory> {
    return mutableListOf(
        JvmCoreModuleFactory(),
        MainModuleFactory(),
        SettingsModuleFactory(),
        JvmSQLModuleFactory()
    ).apply {
        addAll(getCommonModuleFactories(ConfigImp))
    }
}