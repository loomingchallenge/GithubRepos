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
)

internal fun FakeRepositorySearchResponse.getPageData(query: String, page: Int, itemsPerPage: Int): RepositorySearchResponse {
    val filteredItems = if (query == "stars:>0") {
        items
    } else {
        items.filter { repo ->
            listOfNotNull(
                repo.name,
                repo.fullName,
                repo.description,
                repo.url,
                repo.homepage,
                repo.cloneUrl,
                repo.deploymentsUrl,
                repo.language
            ).any { it.contains(query, ignoreCase = true) }
        }
    }

    val firstIndex = itemsPerPage * (page - 1)
    val lastIndex = (firstIndex + itemsPerPage - 1).coerceAtMost(filteredItems.lastIndex)

    val pagedItems = if (firstIndex <= filteredItems.lastIndex) {
        filteredItems.subList(firstIndex, lastIndex + 1).map {
            it.toRepo().toListRepo()
        }
    } else {
        listOf()
    }
    return RepositorySearchResponse(filteredItems.size, pagedItems)
}

internal fun FakeRepositorySearchResponse.getRepo(id: Long): Repo {
    return items.find { it.id == id }?.toRepo()
        ?: throw IllegalArgumentException("Repository with id $id is not in the list.")
}
