package com.githubrepos.di

import com.githubrepos.core.di.createGetRepoUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

class CoreModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            factoryOf(::createGetRepoUseCase)
        }
    }
}