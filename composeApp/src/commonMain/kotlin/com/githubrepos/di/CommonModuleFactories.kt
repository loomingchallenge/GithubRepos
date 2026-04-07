package com.githubrepos.di

import com.githubrepos.Config

fun getCommonModuleFactories(config: Config): List<ModuleFactory> {
    return listOf(
        CoreModuleFactory(),
        DataModuleFactory(),
        SqlModuleFactory(),
        PresentationModuleFactory(config.isPullRefreshSupported),
        UIModuleFactory(),
        NavigationModuleFactory(),
        ApiModuleFactory(config.isDebug)
    )
}