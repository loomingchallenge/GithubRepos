package com.githubrepos.adapter.data.datasource.database.dao

import androidx.paging.PagingSource
import com.githubrepos.core.entity.ListRepo

interface ListRepoDao {
    suspend fun insert(listRepos: List<ListRepo>)

    suspend fun insertWithTransaction(listRepos: List<ListRepo>)

    fun getListRepos(): PagingSource<Int, ListRepo>

    fun getListRepos(queryString: String): PagingSource<Int, ListRepo>

    suspend fun deleteAll()
}
