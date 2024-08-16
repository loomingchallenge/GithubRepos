package com.githubrepos.di

import android.content.Context
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.githubrepos.framework.datasource.database.sqldelight.di.createDatabaseDriverFactory

val androidSQLModule = module {
    singleOf<DatabaseDriverFactory, Context>(::createDatabaseDriverFactory)
}
