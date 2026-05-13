package com.githubrepos.framework.datasource.database.sqldelight

import kotlinx.coroutines.test.TestResult
import kotlin.test.Test

internal class RepoDatabaseIWasmTest : RepoDatabaseTest(
    InMemoryDatabaseDriverFactory()
) {

    @Test
    override fun repoDaoTest(): TestResult {
        return super.repoDaoTest()
    }
}