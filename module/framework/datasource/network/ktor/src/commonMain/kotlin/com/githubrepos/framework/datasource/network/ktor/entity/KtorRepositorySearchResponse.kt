package com.githubrepos.framework.datasource.network.ktor.entity

import com.githubrepos.adapter.data.datasource.network.entity.RepositorySearchResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class KtorRepositorySearchResponse(
    @SerialName("total_count") val total: Int,
    @SerialName("items") val items: List<KtorListRepo>
)

internal fun KtorRepositorySearchResponse.toRepositorySearchResponse() =
    RepositorySearchResponse(
        total,
        items.map { it.toListRepo() }
    )