package com.githubrepos.di

import com.githubrepos.ConfigImp
import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger

fun initKoin() {
    startKoin {
        slf4jLogger()
        modules(
            appModule(ConfigImp),
            jvmMainModule,
            settingsModule,
            jvmSQLModule
        )
    }
}