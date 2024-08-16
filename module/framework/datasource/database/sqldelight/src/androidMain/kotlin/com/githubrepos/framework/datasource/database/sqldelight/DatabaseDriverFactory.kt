package com.githubrepos.framework.datasource.database.sqldelight

import android.content.Context
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(private val context: Context, private val databaseName: String) {

    actual fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>): SqlDriver {
        return AndroidSqliteDriver(schema, context, databaseName)
    }
}