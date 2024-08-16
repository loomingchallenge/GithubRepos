package com.githubrepos.di

import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.githubrepos.framework.datasource.database.sqldelight.di.createDatabaseDriverFactory

val iosSQLModule = module {
    singleOf<DatabaseDriverFactory>(::createDatabaseDriverFactory)
}
