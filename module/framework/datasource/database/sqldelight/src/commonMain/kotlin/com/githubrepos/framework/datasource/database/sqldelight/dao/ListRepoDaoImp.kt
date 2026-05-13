package com.githubrepos.framework.datasource.database.sqldelight.dao

import androidx.paging.PagingSource
import com.githubrepos.adapter.data.datasource.database.dao.ListRepoDao
import com.githubrepos.core.AppDispatchers
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.framework.datasource.database.sqldelight.QueryPagingSource
import com.githubrepos.framework.datasource.database.sqldelight.RepoDatabaseImp

internal class ListRepoDaoImp(
    private val database: RepoDatabaseImp,
    private val dispatchers: AppDispatchers
) : ListRepoDao {

    override suspend fun insert(listRepos: List<ListRepo>) {
        listRepos.forEach { listRepo ->
            database.queries.insertListRepo(
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

    override suspend fun insertWithTransaction(listRepos: List<ListRepo>) {
        database.transaction {
            insert(listRepos)
        }
    }

    override fun getListRepos(): PagingSource<Int, ListRepo> {
        return QueryPagingSource(
            context = dispatchers.io,
            dbTransaction = database::transaction,
            countQuery = database.queries.countListRepos(),
            getItemsQuery = { limit, offset ->
                database.queries.getListRepos(limit, offset, ::createListRepo)
            },
            getItemOffsetQuery = { item ->
                database.queries.getListRepoOffset(item.stars.toLong(), item.name)
            }
        )
    }

    override fun getListRepos(queryString: String): PagingSource<Int, ListRepo> {
        return QueryPagingSource(
            context = dispatchers.io,
            dbTransaction = database::transaction,
            countQuery = database.queries.countListReposByQuery(queryString),
            getItemsQuery = { limit, offset ->
                database.queries.getListReposByQuery(queryString, limit, offset, ::createListRepo)
            },
            getItemOffsetQuery = { item ->
                database.queries.getListRepoOffsetByQuery(queryString, item.stars.toLong(), item.name)
            }
        )
    }

    override suspend fun deleteAll() {
        database.queries.deleteListRepos()
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