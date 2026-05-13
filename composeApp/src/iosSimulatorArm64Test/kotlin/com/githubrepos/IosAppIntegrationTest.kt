package com.githubrepos

import com.githubrepos.di.IosCoreModuleFactory
import com.githubrepos.di.IosSQLModuleFactory
import com.githubrepos.di.KoinManager
import com.githubrepos.di.MainModuleFactory
import com.githubrepos.di.SettingsModuleFactory
import com.githubrepos.di.TestCoreModuleFactory
import com.githubrepos.di.TestIosSQLModuleFactory
import com.githubrepos.di.TestMainModuleFactory
import com.githubrepos.di.TestSettingsModuleFactory
import com.githubrepos.di.getIosModuleFactories
import kotlin.test.Test

class IosAppIntegrationTest: AppIntegrationTest() {

    override val koinManager: KoinManager =
        KoinManager().apply {
            addModuleFactories(getIosModuleFactories())
            replaceModuleFactory(IosCoreModuleFactory::class, TestCoreModuleFactory())
            replaceModuleFactory(MainModuleFactory::class, TestMainModuleFactory())
            replaceModuleFactory(IosSQLModuleFactory::class, TestIosSQLModuleFactory())
            replaceModuleFactory(SettingsModuleFactory::class, TestSettingsModuleFactory())
        }

    @Test
    override fun testNavigation() {
        super.testNavigation()
    }
}