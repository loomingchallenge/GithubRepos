package com.githubrepos.core.repository

import com.githubrepos.core.Lce
import com.githubrepos.core.entity.Repo
import kotlinx.coroutines.flow.Flow

interface RepoRepository {

    fun getRepo(id: Long, freshDataOnly: Boolean = false): Flow<Lce<out Repo>>
}
