package com.githubrepos.core.di

import com.githubrepos.core.repository.RepoRepository
import com.githubrepos.core.usecase.GetRepoUseCase
import com.githubrepos.core.usecase.GetRepoUseCaseImp

fun createGetRepoUseCase(
    repoRepository: RepoRepository
): GetRepoUseCase {
    return GetRepoUseCaseImp(repoRepository)
}
