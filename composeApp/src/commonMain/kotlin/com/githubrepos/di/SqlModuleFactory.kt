package com.githubrepos.di

import com.githubrepos.framework.datasource.database.sqldelight.di.createRepoDatabase
import com.githubrepos.framework.datasource.database.sqldelight.di.createRepoDao
import com.githubrepos.framework.datasource.database.sqldelight.di.createRepoPageDao
import com.githubrepos.framework.datasource.database.sqldelight.di.createListRepoDao
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class SqlModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            singleOf(::createRepoDatabase)
            singleOf(::createRepoDao)
            singleOf(::createRepoPageDao)
            singleOf(::createListRepoDao)
        }
    }
}