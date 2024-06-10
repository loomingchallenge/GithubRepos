package com.githubrepos.framework.datasource.database.sqldelight.di

import com.githubrepos.adapter.data.datasource.database.RepoDatabase
import com.githubrepos.adapter.data.datasource.database.dao.ListRepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoPageDao
import com.githubrepos.core.AppDispatchers
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.RepoDatabaseImp

fun createRepoDatabase(
    databaseDriverFactory: DatabaseDriverFactory,
    dispatchers: AppDispatchers
): RepoDatabase {
    return RepoDatabaseImp(databaseDriverFactory, dispatchers)
}

fun createRepoDao(repoDatabase: RepoDatabase): RepoDao {
    return repoDatabase.repoDao
}

fun createRepoPageDao(repoDatabase: RepoDatabase): RepoPageDao {
    return repoDatabase.repoPageDao
}

fun createListRepoDao(repoDatabase: RepoDatabase): ListRepoDao {
    return repoDatabase.listRepoDao
}