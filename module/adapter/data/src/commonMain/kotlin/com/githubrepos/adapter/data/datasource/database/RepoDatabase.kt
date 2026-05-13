package com.githubrepos.adapter.data.datasource.database

import com.githubrepos.adapter.data.datasource.database.dao.ListRepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoDao
import com.githubrepos.adapter.data.datasource.database.dao.RepoPageDao

interface RepoDatabase: BaseDatabase {

    val repoDao: RepoDao

    val listRepoDao: ListRepoDao

    val repoPageDao: RepoPageDao
}
