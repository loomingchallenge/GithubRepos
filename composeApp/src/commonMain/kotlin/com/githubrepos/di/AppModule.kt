package com.githubrepos.di

import org.koin.dsl.module

fun appModule() = module {
    includes(
        coreModule,
        dataModule,
        demoApiModule,
        sqlModule,
        presentationModule,
        uiModule,
        navigationModule
    )
}