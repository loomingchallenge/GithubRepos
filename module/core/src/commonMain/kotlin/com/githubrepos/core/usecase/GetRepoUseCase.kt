package com.githubrepos.core.usecase

import com.githubrepos.core.Lce
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.repository.RepoRepository
import kotlinx.coroutines.flow.Flow

interface GetRepoUseCase {
    fun execute(id: Long, freshDataOnly: Boolean = false): Flow<Lce<out Repo>>
}

class GetRepoUseCaseImp(
    private val repoRepository: RepoRepository
) : GetRepoUseCase {

    override fun execute(id: Long, freshDataOnly: Boolean): Flow<Lce<out Repo>> {
        return repoRepository.getRepo(id, freshDataOnly)
    }
}
