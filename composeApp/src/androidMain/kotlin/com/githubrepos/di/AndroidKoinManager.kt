package com.githubrepos.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AndroidKoinManager(val context: Context) : KoinManager() {

    override fun startKoin() {
        startKoin {
            androidContext(androidContext = context)
            androidLogger()
            setModules(this)
        }
    }
}