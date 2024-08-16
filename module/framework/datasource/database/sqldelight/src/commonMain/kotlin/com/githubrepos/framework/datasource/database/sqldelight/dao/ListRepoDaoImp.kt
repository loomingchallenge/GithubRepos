package com.githubrepos.framework.datasource.database.sqldelight.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import com.githubrepos.adapter.data.datasource.database.dao.ListRepoDao
import com.githubrepos.core.AppDispatchers
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.framework.datasource.database.sqldelight.DelightRepoDatabaseQueries

internal class ListRepoDaoImp(
    private val queries: DelightRepoDatabaseQueries,
    private val dispatchers: AppDispatchers
) : ListRepoDao {

    override fun insert(listRepos: List<ListRepo>) {
        queries.transaction {
            listRepos.forEach { listRepo ->
                queries.insertListRepo(
                    com.githubrepos.framework.datasource.database.sqldelight.ListRepo(
                        listRepo.id,
                        listRepo.name,
                        listRepo.fullName,
                        listRepo.description,
                        listRepo.stars.toLong(),
                        listRepo.forks.toLong(),
                        listRepo.language
                    )
                )
            }
        }
    }

    override fun getListRepos(): PagingSource<Int, ListRepo> {
        return QueryPagingSource(
            countQuery = queries.countListRepos(),
            transacter = queries,
            context = dispatchers.io,
            queryProvider = { limit, offset ->
                queries.getListRepos(limit, offset, ::createListRepo)
            }
        )
    }

    override fun getListRepos(queryString: String): PagingSource<Int, ListRepo> {
        return QueryPagingSource(
            countQuery = queries.countListReposByName(queryString),
            transacter = queries,
            context = dispatchers.io,
            queryProvider = { limit, offset ->
                queries.getListReposByName(queryString, limit, offset, ::createListRepo)
            }
        )
    }

    override fun deleteAll() {
        queries.deleteListRepos()
    }

    private fun createListRepo(
        id: Long,
        name: String,
        fullName: String,
        description: String?,
        stars: Long,
        forks: Long,
        language: String?
    ): ListRepo {
        return ListRepo(id, name, fullName, description, stars.toInt(), forks.toInt(), language)
    }
}