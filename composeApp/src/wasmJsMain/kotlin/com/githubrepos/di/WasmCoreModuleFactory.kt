package com.githubrepos.di

import com.githubrepos.core.AppDispatchers
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

class WasmCoreModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            single<AppDispatchers> {
                object : AppDispatchers {
                    override val main = Dispatchers.Main
                    override val io = Dispatchers.Default // WASM fallback
                    override val default = Dispatchers.Default
                }
            }
        }
    }
}