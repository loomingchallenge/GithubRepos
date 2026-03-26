package com.githubrepos.framework.datasource.database.sqldelight.dao

import androidx.paging.PagingSource
import com.githubrepos.adapter.data.datasource.database.dao.ListRepoDao
import com.githubrepos.core.AppDispatchers
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.framework.datasource.database.sqldelight.DelightRepoDatabaseQueries
import com.githubrepos.framework.datasource.database.sqldelight.QueryPagingSource

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
            context = dispatchers.io,
            dbTransaction = queries::transaction,
            countQuery = queries.countListRepos(),
            getItemsQuery = { limit, offset ->
                queries.getListRepos(limit, offset, ::createListRepo)
            },
            getItemOffsetQuery = { item ->
                queries.getListRepoOffset(item.stars.toLong(), item.name)
            }
        )
    }

    override fun getListRepos(queryString: String): PagingSource<Int, ListRepo> {
        return QueryPagingSource(
            context = dispatchers.io,
            dbTransaction = queries::transaction,
            countQuery = queries.countListReposByQuery(queryString),
            getItemsQuery = { limit, offset ->
                queries.getListReposByQuery(queryString, limit, offset, ::createListRepo)
            },
            getItemOffsetQuery = { item ->
                queries.getListRepoOffsetByQuery(queryString, item.stars.toLong(), item.name)
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