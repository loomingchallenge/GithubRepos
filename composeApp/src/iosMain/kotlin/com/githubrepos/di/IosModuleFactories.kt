package com.githubrepos.di

import com.githubrepos.ConfigImp

fun getIosModuleFactories(): List<ModuleFactory> {
    return mutableListOf(
        IosCoreModuleFactory(),
        MainModuleFactory(),
        SettingsModuleFactory(),
        IosSQLModuleFactory()
    ).apply {
        addAll(getCommonModuleFactories(ConfigImp))
    }
}