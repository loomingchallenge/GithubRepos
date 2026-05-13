package com.githubrepos.framework.datasource.database.sqldelight.dao

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import com.githubrepos.adapter.data.datasource.database.dao.RepoPageDao
import com.githubrepos.core.entity.RepoPage
import com.githubrepos.framework.datasource.database.sqldelight.RepoDatabaseImp

internal class RepoPageDaoImp(private val database: RepoDatabaseImp): RepoPageDao {

    override suspend fun insert(repoPages: List<RepoPage>) {
        repoPages.forEach { repoPage ->
            database.queries.insertRepoPage(
                com.githubrepos.framework.datasource.database.sqldelight.RepoPage(
                    repoPage.repoId,
                    repoPage.page?.toLong()
                )
            )
        }
    }

    override suspend fun insertWithTransaction(repoPages: List<RepoPage>) {
        database.transaction {
            insert(repoPages)
        }
    }

    override suspend fun getRepoPage(repoId: Long): RepoPage? {
        return database.queries.getRepoPage(repoId, ::createRepoPage).awaitAsOneOrNull()
    }

    override suspend fun getMinPage(): Long? {
        return database.queries.getMinPage().awaitAsOneOrNull()?.min
    }

    override suspend fun getMaxPage(): Long? {
        return database.queries.getMaxPage().awaitAsOneOrNull()?.max
    }

    override suspend fun deleteAll() {
        database.queries.deleteRepoPages()
    }

    private fun createRepoPage(
        repoId: Long,
        page: Long?
    ): RepoPage {
        return RepoPage(repoId, page?.toInt())
    }
}