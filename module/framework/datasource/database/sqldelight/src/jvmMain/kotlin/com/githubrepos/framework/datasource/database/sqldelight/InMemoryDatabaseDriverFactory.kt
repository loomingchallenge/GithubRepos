package com.githubrepos.framework.datasource.database.sqldelight

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.sqldelight.db.QueryResult

internal class InMemoryDatabaseDriverFactory : DatabaseDriverFactory {

    override fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        schema.create(driver)
        return driver
    }
}