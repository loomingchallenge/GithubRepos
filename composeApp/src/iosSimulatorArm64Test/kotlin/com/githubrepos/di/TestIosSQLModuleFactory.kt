package com.githubrepos.di

import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.di.createInMemoryDatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class TestIosSQLModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            singleOf<DatabaseDriverFactory>(::createInMemoryDatabaseDriverFactory)
        }
    }
}