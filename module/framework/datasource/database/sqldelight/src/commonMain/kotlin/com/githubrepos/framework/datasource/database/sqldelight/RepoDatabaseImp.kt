package com.githubrepos.framework.datasource.database.sqldelight

import app.cash.sqldelight.db.SqlDriver
import com.githubrepos.adapter.data.datasource.database.BaseDatabase
import com.githubrepos.adapter.data.datasource.database.RepoDatabase
import com.githubrepos.adapter.data.datasource.database.dao.ListRepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoPageDao
import com.githubrepos.core.AppDispatchers
import com.githubrepos.framework.datasource.database.sqldelight.dao.ListRepoDaoImp
import com.githubrepos.framework.datasource.database.sqldelight.dao.RepoDaoImp
import com.githubrepos.framework.datasource.database.sqldelight.dao.RepoPageDaoImp
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal const val REPO_DATABASE_NAME = "DelightRepoDatabase.db"

internal class RepoDatabaseImp(
    val databaseDriverFactory: DatabaseDriverFactory,
    dispatchers: AppDispatchers
): RepoDatabase {

    override val repoDao: RepoDao = RepoDaoImp(this, dispatchers)

    override val listRepoDao: ListRepoDao = ListRepoDaoImp(this, dispatchers)

    override val repoPageDao: RepoPageDao = RepoPageDaoImp(this)

    lateinit var queries: DelightRepoDatabaseQueries

    private var driver: SqlDriver? = null

    private val transactionMutex = Mutex() // Only Wasm requires this. Android, iOS can have different implementation.

    override suspend fun init() {
        if (driver == null) {
            val driver = databaseDriverFactory.createDriver(DelightRepoDatabase.Schema)
            this.driver = driver
            queries = DelightRepoDatabase(driver).delightRepoDatabaseQueries
        }
    }

    override suspend fun transaction(body: suspend BaseDatabase.() -> Unit) {
        transactionMutex.withLock {
            return queries.transaction {
                body()
            }
        }
    }

    override fun close() {
        driver?.close()
    }
}