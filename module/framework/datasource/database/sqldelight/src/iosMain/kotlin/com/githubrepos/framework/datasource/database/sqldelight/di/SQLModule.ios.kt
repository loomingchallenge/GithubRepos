package com.githubrepos.framework.datasource.database.sqldelight.di

import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactoryImp
import com.githubrepos.framework.datasource.database.sqldelight.InMemoryDatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.REPO_DATABASE_NAME

fun createDatabaseDriverFactory(databaseName: String = REPO_DATABASE_NAME): DatabaseDriverFactory {
    return DatabaseDriverFactoryImp(databaseName)
}

fun createInMemoryDatabaseDriverFactory(): DatabaseDriverFactory {
    return InMemoryDatabaseDriverFactory()
}