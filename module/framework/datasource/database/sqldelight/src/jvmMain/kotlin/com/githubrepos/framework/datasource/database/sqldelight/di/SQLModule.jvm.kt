package com.githubrepos.framework.datasource.database.sqldelight.di

import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactoryImp
import com.githubrepos.framework.datasource.database.sqldelight.InMemoryDatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.REPO_DATABASE_NAME

fun createDatabaseDriverFactory(appName: String, databaseName: String = REPO_DATABASE_NAME): DatabaseDriverFactory {
    return DatabaseDriverFactoryImp(appName, databaseName)
}

fun createInMemoryDatabaseDriverFactory(): DatabaseDriverFactory {
    return InMemoryDatabaseDriverFactory()
}