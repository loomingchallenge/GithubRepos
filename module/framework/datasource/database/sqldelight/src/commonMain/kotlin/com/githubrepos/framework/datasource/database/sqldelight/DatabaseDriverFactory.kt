package com.githubrepos.framework.datasource.database.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

interface DatabaseDriverFactory {

    fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>): SqlDriver
}