package com.githubrepos.framework.datasource.database.sqldelight.di

import android.content.Context
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactoryImp
import com.githubrepos.framework.datasource.database.sqldelight.REPO_DATABASE_NAME

fun createDatabaseDriverFactory(context: Context, databaseName: String = REPO_DATABASE_NAME): DatabaseDriverFactory {
    return DatabaseDriverFactoryImp(context, databaseName)
}

fun createInMemoryDatabaseDriverFactory(context: Context): DatabaseDriverFactory {
    return DatabaseDriverFactoryImp(context, null)
}