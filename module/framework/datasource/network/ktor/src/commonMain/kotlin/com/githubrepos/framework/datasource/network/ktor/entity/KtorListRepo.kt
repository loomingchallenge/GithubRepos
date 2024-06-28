package com.githubrepos.framework.datasource.network.ktor.entity

import com.githubrepos.core.entity.ListRepo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class KtorListRepo(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("description") val description: String?,
    @SerialName("stargazers_count") val stars: Int,
    @SerialName("forks_count") val forks: Int,
    @SerialName("language") val language: String?
)

internal fun KtorListRepo.toListRepo() =
    ListRepo(id, name, fullName, description, stars, forks, language)