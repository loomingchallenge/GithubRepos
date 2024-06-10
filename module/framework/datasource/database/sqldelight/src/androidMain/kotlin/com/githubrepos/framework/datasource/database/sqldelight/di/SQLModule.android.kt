package com.githubrepos.framework.datasource.database.sqldelight.di

import android.content.Context
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory

fun createDatabaseDriverFactory(context: Context): DatabaseDriverFactory {
    return DatabaseDriverFactory(context)
}