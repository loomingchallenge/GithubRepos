package com.githubrepos.framework.datasource.database.sqldelight

import com.githubrepos.adapter.data.datasource.database.RepoDatabase
import com.githubrepos.adapter.data.datasource.database.dao.ListRepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoPageDao
import com.githubrepos.core.AppDispatchers
import com.githubrepos.framework.datasource.database.sqldelight.dao.ListRepoDaoImp
import com.githubrepos.framework.datasource.database.sqldelight.dao.RepoDaoImp
import com.githubrepos.framework.datasource.database.sqldelight.dao.RepoPageDaoImp

internal const val REPO_DATABASE_NAME = "DelightRepoDatabase.db"

internal class RepoDatabaseImp(databaseDriverFactory: DatabaseDriverFactory, dispatchers: AppDispatchers): RepoDatabase {

    private val driver = databaseDriverFactory.createDriver(DelightRepoDatabase.Schema)

    private val queries = DelightRepoDatabase(driver).delightRepoDatabaseQueries

    override val repoDao: RepoDao = RepoDaoImp(queries, dispatchers)

    override val listRepoDao: ListRepoDao = ListRepoDaoImp(queries, dispatchers)

    override val repoPageDao: RepoPageDao = RepoPageDaoImp(queries)

    override fun <R> withTransaction(block: () -> R): R {
        return queries.transactionWithResult {
            block()
        }
    }

    override fun close() {
        driver.close()
    }
}