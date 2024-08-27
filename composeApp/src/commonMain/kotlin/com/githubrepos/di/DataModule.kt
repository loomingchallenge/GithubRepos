package com.githubrepos.di

import com.githubrepos.adapter.data.datasource.database.RepoDatabase
import com.githubrepos.adapter.data.di.createRepoRepository
import com.githubrepos.adapter.data.di.createSearchReposRepository
import com.githubrepos.core.repository.SearchReposRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::createRepoRepository)
    single<SearchReposRepository> { createSearchReposRepository(get(), get(), get(), get<RepoDatabase>(), get(), get()) }
}

