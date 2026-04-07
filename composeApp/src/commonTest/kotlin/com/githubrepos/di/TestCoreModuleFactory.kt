package com.githubrepos.di

import com.githubrepos.core.AppDispatchers
import com.githubrepos.core.di.createGetRepoUseCase
import com.githubrepos.core.test.TestAppDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

class TestCoreModuleFactory: ModuleFactory {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun create(): Module {
        return module {
            single<AppDispatchers> { TestAppDispatchers(Dispatchers.Main) }
            factoryOf(::createGetRepoUseCase)
        }
    }
}