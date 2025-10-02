package com.githubrepos.di

import com.githubrepos.Config
import com.githubrepos.flavor.di.createFlavorModules
import org.koin.dsl.module

fun appModule(config: Config) = module {
    includes(
        coreModule,
        dataModule,
        sqlModule,
        createPresentationModule(config.isPullRefreshSupported),
        uiModule,
        navigationModule,
        *createFlavorModules(config.isDebug).toTypedArray()
    )
}