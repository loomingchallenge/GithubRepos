package com.githubrepos.di

import com.githubrepos.core.AppDispatchers
import com.githubrepos.core.DefaultAppDispatchers
import com.githubrepos.core.di.createGetRepoUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::DefaultAppDispatchers) { bind<AppDispatchers>() }
    factoryOf(::createGetRepoUseCase)
}
