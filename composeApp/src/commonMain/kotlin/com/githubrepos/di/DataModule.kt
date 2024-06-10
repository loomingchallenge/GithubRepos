package com.githubrepos.di

import com.githubrepos.adapter.data.di.createRepoRepository
import com.githubrepos.adapter.data.di.createSearchReposRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::createRepoRepository)
    singleOf(::createSearchReposRepository)
}

