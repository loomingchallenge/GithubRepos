package com.githubrepos.di

import com.githubrepos.adapter.data.datasource.database.RepoDatabase
import com.githubrepos.adapter.data.datasource.database.dao.ListRepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoPageDao
import com.githubrepos.framework.datasource.database.sqldelight.di.createRepoDatabase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class SqlModuleFactory: ModuleFactory {
    override fun create(): Module {
        return module {
            singleOf(::createRepoDatabase)
            single<RepoDao> { get<RepoDatabase>().repoDao }
            single<RepoPageDao> { get<RepoDatabase>().repoPageDao }
            single<ListRepoDao> { get<RepoDatabase>().listRepoDao }
        }
    }
}