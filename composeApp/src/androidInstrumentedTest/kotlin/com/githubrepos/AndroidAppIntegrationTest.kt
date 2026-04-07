package com.githubrepos

import androidx.test.core.app.ApplicationProvider
import com.githubrepos.di.AndroidKoinManager
import com.githubrepos.di.AndroidSQLModuleFactory
import com.githubrepos.di.KoinManager
import com.githubrepos.di.MainModuleFactory
import com.githubrepos.di.SettingsModuleFactory
import com.githubrepos.di.TestAndroidSQLModuleFactory
import com.githubrepos.di.TestMainModuleFactory
import com.githubrepos.di.TestSettingsModuleFactory
import com.githubrepos.di.getAndroidModuleFactories
import kotlin.test.Test

class AndroidAppIntegrationTest: AppIntegrationTest() {

    override val koinManager: KoinManager =
        AndroidKoinManager(ApplicationProvider.getApplicationContext()).apply {
            addModuleFactories(getAndroidModuleFactories())
            replaceModuleFactory(AndroidSQLModuleFactory::class, TestAndroidSQLModuleFactory())
            replaceModuleFactory(MainModuleFactory::class, TestMainModuleFactory())
            replaceModuleFactory(SettingsModuleFactory::class, TestSettingsModuleFactory())
        }

    @Test
    override fun testNavigation() {
        super.testNavigation()
    }
}