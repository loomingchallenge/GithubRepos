package com.githubrepos.framework.datasource.database.sqldelight.di

import com.githubrepos.adapter.data.datasource.database.RepoDatabase
import com.githubrepos.core.AppDispatchers
import com.githubrepos.framework.datasource.database.sqldelight.DatabaseDriverFactory
import com.githubrepos.framework.datasource.database.sqldelight.RepoDatabaseImp

fun createRepoDatabase(
    databaseDriverFactory: DatabaseDriverFactory,
    dispatchers: AppDispatchers
): RepoDatabase {
    return RepoDatabaseImp(databaseDriverFactory, dispatchers)
}