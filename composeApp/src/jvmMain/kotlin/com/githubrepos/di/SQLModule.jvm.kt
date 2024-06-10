package com.githubrepos.di

import APP_FOLDER_NAME
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.di.createDatabaseDriverFactory
import org.koin.dsl.module

val jvmSQLModule = module {
    single<DatabaseDriverFactory> {
        createDatabaseDriverFactory(APP_FOLDER_NAME)
    }
}
