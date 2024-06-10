package com.githubrepos

import android.app.Application
import com.githubrepos.di.initKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(context = applicationContext)
    }
}
