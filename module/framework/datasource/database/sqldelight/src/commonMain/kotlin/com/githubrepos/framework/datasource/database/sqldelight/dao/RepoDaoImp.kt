package com.githubrepos.framework.datasource.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.githubrepos.adapter.data.datasource.database.dao.RepoDao
import com.githubrepos.core.AppDispatchers
import com.githubrepos.core.entity.Repo
import com.githubrepos.framework.datasource.database.sqldelight.DelightRepoDatabaseQueries
import kotlinx.coroutines.flow.Flow

internal class RepoDaoImp(
    private val queries: DelightRepoDatabaseQueries,
    private val dispatchers: AppDispatchers
): RepoDao {

    override fun insert(repo: Repo) {
        queries.insertRepo(
            com.githubrepos.framework.datasource.database.sqldelight.Repo(
                repo.id,
                repo.name,
                repo.fullName,
                repo.description,
                repo.url,
                repo.homepage,
                repo.cloneUrl,
                repo.deploymentsUrl,
                repo.stars.toLong(),
                repo.forks.toLong(),
                repo.language
            )
        )
    }

    override fun delete(id: Long) {
        queries.deleteRepo(id)
    }

    override fun deleteAll() {
        queries.deleteRepos()
    }

    override fun getRepo(id: Long): Flow<Repo?> {
        return queries.getRepo(id, ::createRepo)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
    }

    private fun createRepo(
        id: Long,
        name: String,
        fullName: String,
        description: String?,
        url: String?,
        homepage: String?,
        cloneUrl: String?,
        deploymentsUrl: String?,
        stars: Long,
        forks: Long,
        language: String?,
    ): Repo {
        return Repo(id, name, fullName, description, url, homepage, cloneUrl, deploymentsUrl, stars.toInt(), forks.toInt(), language)
    }
}