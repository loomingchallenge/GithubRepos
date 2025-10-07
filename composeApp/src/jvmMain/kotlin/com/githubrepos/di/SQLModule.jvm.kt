package com.githubrepos.di

import com.githubrepos.flavor.jvmAppName
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.di.createDatabaseDriverFactory
import org.koin.dsl.module

val jvmSQLModule = module {
    single<DatabaseDriverFactory> {
        createDatabaseDriverFactory(jvmAppName)
    }
}
