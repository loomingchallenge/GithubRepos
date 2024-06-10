package com.githubrepos.adapter.data.datasource.network.entity

import com.githubrepos.core.entity.ListRepo

data class RepositorySearchResponse(
    val total: Int,
    val items: List<ListRepo>
)
