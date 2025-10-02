package com.githubrepos.di

import com.githubrepos.ConfigImp
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            appModule(ConfigImp),
            iosMainModule,
            settingsModule,
            iosSQLModule
        )
    }
}