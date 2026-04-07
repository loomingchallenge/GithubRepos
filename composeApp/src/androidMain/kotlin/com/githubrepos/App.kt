package com.githubrepos

import android.app.Application
import com.githubrepos.di.AndroidKoinManager
import com.githubrepos.di.getAndroidModuleFactories

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidKoinManager(applicationContext).run {
            addModuleFactories(getAndroidModuleFactories())
            startKoin()
        }
    }
}
