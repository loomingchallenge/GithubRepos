package com.githubrepos.di

import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.githubrepos.framework.datasource.database.sqldelight.di.createDatabaseDriverFactory
import org.koin.core.module.Module

class IosSQLModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            singleOf<DatabaseDriverFactory>(::createDatabaseDriverFactory)
        }
    }
}