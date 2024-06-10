package com.githubrepos.core.repository

import androidx.paging.PagingData
import com.githubrepos.core.entity.ListRepo
import kotlinx.coroutines.flow.Flow

interface SearchReposRepository {

    fun getRepositoryFlow(query: String): Flow<PagingData<ListRepo>>
}
