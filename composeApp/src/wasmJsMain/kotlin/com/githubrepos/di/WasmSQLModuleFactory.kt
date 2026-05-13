package com.githubrepos.di

import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.di.createDatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

class WasmSQLModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            single<DatabaseDriverFactory> { createDatabaseDriverFactory() }
        }
    }
}