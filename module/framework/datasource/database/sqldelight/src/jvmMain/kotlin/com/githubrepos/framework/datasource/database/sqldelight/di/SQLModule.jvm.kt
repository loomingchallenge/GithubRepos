package com.githubrepos.framework.datasource.database.sqldelight.di

import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory

fun createDatabaseDriverFactory(appName: String): DatabaseDriverFactory {
    return DatabaseDriverFactory(appName)
}