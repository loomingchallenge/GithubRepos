package com.githubrepos.di

import com.githubrepos.ConfigImp

fun getJvmModuleFactories(): List<ModuleFactory> {
    return mutableListOf(
        MainModuleFactory(),
        SettingsModuleFactory(),
        JvmSQLModuleFactory()
    ).apply {
        addAll(getCommonModuleFactories(ConfigImp))
    }
}