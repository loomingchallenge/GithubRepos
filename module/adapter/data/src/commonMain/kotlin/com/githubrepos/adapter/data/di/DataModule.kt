package com.githubrepos.adapter.data.di

import com.githubrepos.adapter.data.datasource.database.DBWithTransaction
import com.githubrepos.adapter.data.datasource.database.dao.ListRepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoPageDao
import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.githubrepos.adapter.data.repository.RepoRepositoryImp
import com.githubrepos.adapter.data.repository.SearchReposRepositoryImp
import com.githubrepos.core.AppDispatchers
import com.githubrepos.core.repository.RepoRepository
import com.githubrepos.core.repository.SearchReposRepository

fun createRepoRepository(
    networkStateProvider: NetworkStateProvider,
    githubService: GithubService,
    repoDao: RepoDao
): RepoRepository {
    return RepoRepositoryImp(networkStateProvider, githubService, repoDao)
}

fun createSearchReposRepository(
    service: GithubService,
    listRepoDao: ListRepoDao,
    repoPageDao: RepoPageDao,
    dbWithTransaction: DBWithTransaction,
    preferences: RepoPreferences,
    dispatchers: AppDispatchers,
): SearchReposRepository {
    return SearchReposRepositoryImp(service, listRepoDao, repoPageDao, dbWithTransaction, preferences, dispatchers)
}

