package com.githubrepos.adapter.data.test.datasource.database

import com.githubrepos.adapter.data.datasource.database.BaseDatabase

class FakeDatabase: BaseDatabase {
    override suspend fun init() {
    }

    override suspend fun transaction(body: suspend BaseDatabase.() -> Unit) {
    }

    override fun close() {
    }
}