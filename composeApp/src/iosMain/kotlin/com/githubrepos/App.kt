package com.githubrepos

import com.githubrepos.di.KoinManager
import com.githubrepos.di.getIosModuleFactories

fun initApp() {
    KoinManager().run {
        addModuleFactories(getIosModuleFactories())
        startKoin()
    }
}