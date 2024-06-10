package com.githubrepos.framework.datasource.database.sqldelight.di

import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory

fun createDatabaseDriverFactory(): DatabaseDriverFactory {
    return DatabaseDriverFactory()
}