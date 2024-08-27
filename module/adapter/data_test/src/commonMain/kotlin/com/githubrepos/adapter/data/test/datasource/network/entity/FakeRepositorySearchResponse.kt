package com.githubrepos.adapter.data.test.datasource.network.entity

import com.githubrepos.adapter.data.datasource.network.entity.RepositorySearchResponse
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.entity.toListRepo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FakeRepositorySearchResponse(
    @SerialName("total_count") val total: Int,
    @SerialName("items") val items: List<FakeRepo>
) {

    fun getPageData(page: Int, itemsPerPage: Int): RepositorySearchResponse {
        val firstIndex = itemsPerPage * (page - 1)
        val lastIndex = (firstIndex + itemsPerPage - 1).coerceAtMost(items.lastIndex)
        val items = if (firstIndex <= items.lastIndex) {
            items.subList(firstIndex, lastIndex + 1).map {
                it.toRepo().toListRepo()
            }
        } else {
            listOf()
        }
        return RepositorySearchResponse(total, items)
    }

    fun getRepo(id: Long): Repo {
        return items.find { it.id == id }?.toRepo()
            ?: throw IllegalArgumentException("Repository with id $id is not in the list.")
    }
}
