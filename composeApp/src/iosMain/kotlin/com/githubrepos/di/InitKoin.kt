package com.githubrepos.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            appModule(),
            iosMainModule,
            settingsModule,
            iosSQLModule
        )
    }
}