package com.githubrepos.framework.datasource.database.sqldelight.di

import android.content.Context
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.REPO_DATABASE_NAME

fun createDatabaseDriverFactory(context: Context, databaseName: String = REPO_DATABASE_NAME): DatabaseDriverFactory {
    return DatabaseDriverFactory(context, databaseName)
}