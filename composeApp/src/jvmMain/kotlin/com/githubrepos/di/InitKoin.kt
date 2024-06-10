package com.githubrepos.di

import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger

fun initKoin() {
    startKoin {
        slf4jLogger()
        modules(
            appModule(),
            jvmMainModule,
            settingsModule,
            jvmSQLModule
        )
    }
}