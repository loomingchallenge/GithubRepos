package com.githubrepos.framework.datasource.network.ktor.entity

import com.githubrepos.core.entity.Repo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class KtorRepo(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("description") val description: String?,
    @SerialName("html_url") val url: String?,
    @SerialName("homepage") val homepage: String?,
    @SerialName("clone_url") val cloneUrl: String?,
    @SerialName("deployments_url") val deploymentsUrl: String?,
    @SerialName("stargazers_count") val stars: Int,
    @SerialName("forks_count") val forks: Int,
    @SerialName("language") val language: String?
)

internal fun KtorRepo.toRepo(): Repo {
    return Repo(id, name, fullName, description, url, homepage, cloneUrl, deploymentsUrl, stars, forks, language)
}