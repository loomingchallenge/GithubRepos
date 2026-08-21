package com.githubrepos.core.repository

import androidx.paging.PagingData
import com.githubrepos.core.entity.ListRepo
import kotlinx.coroutines.flow.Flow

interface SearchReposRepository {

    val lastQuery: String

    fun getRepositoryFlow(query: String): Flow<PagingData<ListRepo>>
}
