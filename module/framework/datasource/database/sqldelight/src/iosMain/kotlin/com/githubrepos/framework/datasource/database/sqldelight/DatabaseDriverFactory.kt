package com.githubrepos.framework.datasource.database.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory(private val databaseName: String) {

    actual fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>): SqlDriver {
        return NativeSqliteDriver(schema, databaseName)
    }
}