package com.githubrepos.di

import com.githubrepos.flavor.di.flavorModules
import org.koin.dsl.module

fun appModule() = module {
    includes(
        coreModule,
        dataModule,
        sqlModule,
        presentationModule,
        uiModule,
        navigationModule,
        *flavorModules.toTypedArray()
    )
}