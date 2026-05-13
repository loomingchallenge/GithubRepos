package com.githubrepos.framework.datasource.database.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.worker.createDefaultWebWorkerDriver

internal class DatabaseDriverFactoryImp(private val databaseName: String): DatabaseDriverFactory {

    @OptIn(ExperimentalWasmJsInterop::class)
    override suspend fun createDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): SqlDriver {
        return createDefaultWebWorkerDriver() // Only in memory driver for now.
            .also { schema.create(it).await() }
    }
}