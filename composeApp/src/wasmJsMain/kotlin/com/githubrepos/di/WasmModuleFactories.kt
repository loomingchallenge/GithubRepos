package com.githubrepos.di

import com.githubrepos.ConfigImp

fun getWasmModuleFactories(): List<ModuleFactory> {
    return mutableListOf(
        WasmCoreModuleFactory(),
        MainModuleFactory(),
        SettingsModuleFactory(),
        WasmSQLModuleFactory()
    ).apply {
        addAll(getCommonModuleFactories(ConfigImp))
    }
}