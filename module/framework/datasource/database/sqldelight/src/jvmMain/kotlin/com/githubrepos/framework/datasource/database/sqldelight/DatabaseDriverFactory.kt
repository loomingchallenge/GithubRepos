package com.githubrepos.framework.datasource.database.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

class DatabaseDriverFactoryImp(private val appName: String, private val databaseName: String): DatabaseDriverFactory {

    override fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>): SqlDriver {
        val path = getDatabaseFile(appName, databaseName).absolutePath
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${path}")
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