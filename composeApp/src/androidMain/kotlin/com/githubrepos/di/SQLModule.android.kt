package com.githubrepos.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.githubrepos.framework.datasource.database.sqldelight.di.createDatabaseDriverFactory

val androidSQLModule = module {
    singleOf(::createDatabaseDriverFactory)
}
