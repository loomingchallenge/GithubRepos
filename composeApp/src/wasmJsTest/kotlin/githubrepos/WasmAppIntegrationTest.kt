package com.githubrepos

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.githubrepos.di.KoinManager
import com.githubrepos.di.MainModuleFactory
import com.githubrepos.di.SettingsModuleFactory
import com.githubrepos.di.TestCoreModuleFactory
import com.githubrepos.di.TestWasmSQLModuleFactory
import com.githubrepos.di.TestMainModuleFactory
import com.githubrepos.di.TestSettingsModuleFactory
import com.githubrepos.di.WasmCoreModuleFactory
import com.githubrepos.di.WasmSQLModuleFactory
import com.githubrepos.di.getWasmModuleFactories
import kotlin.test.Ignore

class WasmAppIntegrationTest: AppIntegrationTest() {

    override val koinManager: KoinManager =
        KoinManager().apply {
            addModuleFactories(getWasmModuleFactories())
            replaceModuleFactory(WasmCoreModuleFactory::class, TestCoreModuleFactory())
            replaceModuleFactory(MainModuleFactory::class, TestMainModuleFactory())
            replaceModuleFactory(WasmSQLModuleFactory::class, TestWasmSQLModuleFactory())
            replaceModuleFactory(SettingsModuleFactory::class, TestSettingsModuleFactory())
        }

    @OptIn(ExperimentalWasmJsInterop::class, ExperimentalTestApi::class)
    @Ignore
    override fun testPullRefresh() = runComposeUiTest {
        println("Pull refresh not supported on Wasm")
    }
}