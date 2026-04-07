package com.githubrepos.di

import org.koin.logger.slf4jLogger
import org.koin.core.context.startKoin

class JvmKoinManager: KoinManager() {

    override fun startKoin() {
        startKoin {
            slf4jLogger()
            setModules(this)
        }
    }
}