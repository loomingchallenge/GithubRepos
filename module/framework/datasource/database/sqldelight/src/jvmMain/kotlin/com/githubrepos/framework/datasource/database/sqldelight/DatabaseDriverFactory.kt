package com.githubrepos.framework.datasource.database.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory(private val appName: String, private val databaseName: String) {

    actual fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${getDatabaseFile(appName, databaseName).absolutePath}")
        schema.create(driver)
        return driver
    }
}

private fun getDatabaseFile(appName: String, dbName: String): File {
    return File(
        getAppDir(appName).also { if (!it.exists()) it.mkdirs() },
        dbName
    )
}

private fun getAppDir(appName: String): File {
    val os = System.getProperty("os.name").lowercase()
    return when {
        os.contains("win") -> {
            File(System.getenv("AppData"), appName)
        }

        os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
            File(System.getProperty("user.home"), ".${appName}")
        }

        os.contains("mac") -> {
            File(System.getProperty("user.home"), "Library/Application Support/${appName}")
        }

        else -> error("Unsupported operating system.")
    }
}