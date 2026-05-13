package com.githubrepos.framework.datasource.database.sqldelight

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.driver.native.wrapConnection
import co.touchlab.sqliter.DatabaseConfiguration

internal class InMemoryDatabaseDriverFactory : DatabaseDriverFactory {

    override suspend fun createDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): SqlDriver {
        return NativeSqliteDriver(
            configuration = DatabaseConfiguration(
                name = "Test$REPO_DATABASE_NAME",
                version = schema.synchronous().version.toInt(),
                create = { connection ->
                    wrapConnection(connection) { schema.synchronous().create(it) }
                },
                upgrade = { connection, oldVersion, newVersion ->
                    wrapConnection(connection) {
                        schema.synchronous().migrate(it, oldVersion.toLong(), newVersion.toLong())
                    }
                },
                inMemory = true
            )
        )
    }
}