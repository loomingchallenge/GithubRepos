package com.githubrepos.framework.datasource.database.sqldelight.di

import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.REPO_DATABASE_NAME
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactoryImp

fun createDatabaseDriverFactory(databaseName: String = REPO_DATABASE_NAME): DatabaseDriverFactory {
    return DatabaseDriverFactoryImp(databaseName)
}