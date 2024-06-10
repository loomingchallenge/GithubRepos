package com.githubrepos.framework.datasource.database.sqldelight.dao

import com.githubrepos.adapter.data.datasource.database.dao.RepoPageDao
import com.githubrepos.core.entity.RepoPage
import com.githubrepos.framework.datasource.database.sqldelight.DelightRepoDatabaseQueries

internal class RepoPageDaoImp(private val queries: DelightRepoDatabaseQueries): RepoPageDao {

    override fun insert(repoPages: List<RepoPage>) {
        queries.transaction {
            repoPages.forEach { repoPage ->
                queries.insertRepoPage(
                    com.githubrepos.framework.datasource.database.sqldelight.RepoPage(
                        repoPage.repoId,
                        repoPage.page?.toLong()
                    )
                )
            }
        }
    }

    override fun getRepoPage(repoId: Long): RepoPage? {
        return queries.getRepoPage(repoId, ::createRepoPage).executeAsOneOrNull()
    }

    override fun deleteAll() {
        queries.deleteRepoPages()
    }

    private fun createRepoPage(
        repoId: Long,
        page: Long?
    ): RepoPage {
        return RepoPage(repoId, page?.toInt())
    }
}