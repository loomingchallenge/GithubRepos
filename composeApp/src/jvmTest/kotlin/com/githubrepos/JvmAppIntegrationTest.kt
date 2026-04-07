package com.githubrepos

import com.githubrepos.di.CoreModuleFactory
import com.githubrepos.di.JvmKoinManager
import com.githubrepos.di.JvmSQLModuleFactory
import com.githubrepos.di.KoinManager
import com.githubrepos.di.MainModuleFactory
import com.githubrepos.di.SettingsModuleFactory
import com.githubrepos.di.TestCoreModuleFactory
import com.githubrepos.di.TestJvmSQLModuleFactory
import com.githubrepos.di.TestMainModuleFactory
import com.githubrepos.di.TestSettingsModuleFactory
import com.githubrepos.di.getJvmModuleFactories
import kotlin.test.Ignore

class JvmAppIntegrationTest: AppIntegrationTest() {

    override val koinManager: KoinManager =
        JvmKoinManager().apply {
            addModuleFactories(getJvmModuleFactories())
            replaceModuleFactory(CoreModuleFactory::class, TestCoreModuleFactory())
            replaceModuleFactory(MainModuleFactory::class, TestMainModuleFactory())
            replaceModuleFactory(JvmSQLModuleFactory::class, TestJvmSQLModuleFactory())
            replaceModuleFactory(SettingsModuleFactory::class, TestSettingsModuleFactory())
        }

    @Ignore("Pull refresh not supported on JVM")
    override fun testPullRefresh() {
        println("Pull refresh not supported on JVM")
    }
}