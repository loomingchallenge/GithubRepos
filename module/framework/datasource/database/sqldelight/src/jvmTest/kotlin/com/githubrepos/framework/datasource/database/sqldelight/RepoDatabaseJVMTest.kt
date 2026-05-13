package com.githubrepos.framework.datasource.database.sqldelight

import kotlin.test.Test

internal const val APP_FOLDER_NAME = "GithubReposTests"

internal class RepoDatabaseJVMTest: RepoDatabaseTest(DatabaseDriverFactoryImp(APP_FOLDER_NAME, TEST_REPO_DATABASE_NAME)) {

    @Test
    override fun repoDaoTest() {
        return super.repoDaoTest()
    }
}