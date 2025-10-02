package com.githubrepos.di

import android.content.Context
import com.githubrepos.ConfigImp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

fun initKoin(context: Context) {
    startKoin {
        androidContext(context)
        androidLogger()
        modules(
            appModule(ConfigImp),
            androidMainModule,
            settingsModule,
            androidSQLModule
        )
    }
}