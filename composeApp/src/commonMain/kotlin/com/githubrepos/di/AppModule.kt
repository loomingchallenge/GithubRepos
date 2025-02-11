package com.githubrepos.di

import com.githubrepos.Config
import com.githubrepos.flavor.di.createFlavorModules
import org.koin.dsl.module

fun appModule() = module {
    includes(
        coreModule,
        dataModule,
        sqlModule,
        createPresentationModule(Config.isPullRefreshSupported),
        uiModule,
        navigationModule,
        *createFlavorModules(Config.isDebug).toTypedArray()
    )
}