package com.githubrepos.framework.datasource.database.sqldelight

internal const val APP_FOLDER_NAME = "GithubReposTests"

internal class RepoDatabaseJVMTest : RepoDatabaseTest(DatabaseDriverFactory(APP_FOLDER_NAME, TEST_REPO_DATABASE_NAME))