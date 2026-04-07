package com.githubrepos.di

import android.content.Context
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.di.createDatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class AndroidSQLModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            singleOf<DatabaseDriverFactory, Context>(::createDatabaseDriverFactory)
        }
    }
}