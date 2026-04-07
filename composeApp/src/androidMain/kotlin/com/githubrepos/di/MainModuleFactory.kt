package com.githubrepos.di

import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import com.githubrepos.network.NetworkStateProviderImp
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class MainModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            singleOf(::NetworkStateProviderImp) { bind<NetworkStateProvider>() }
        }
    }
}