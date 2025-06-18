package com.githubrepos.framework.datasource.database.sqldelight

import app.cash.paging.PagingSourceLoadParamsRefresh
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultPage
import com.githubrepos.adapter.data.datasource.database.RepoDatabase
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.test.TestAppDispatchers
import com.githubrepos.core.test.repo0
import com.githubrepos.core.test.repo1
import com.githubrepos.core.test.repoPage0
import com.githubrepos.core.test.repoPage1
import com.githubrepos.framework.datasource.database.sqldelight.di.createRepoDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


internal const val TEST_REPO_DATABASE_NAME = "TEST$REPO_DATABASE_NAME"

internal abstract class RepoDatabaseTest(private val databaseDriverFactory: DatabaseDriverFactory) {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repoDatabase: RepoDatabase

    @BeforeTest
    fun setUp() {
        repoDatabase = createRepoDatabase(databaseDriverFactory, TestAppDispatchers(testDispatcher))
    }

    @AfterTest
    fun tearDown() {
        repoDatabase.close()
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun repoDaoTest() = runTest(testDispatcher) {
        val repoDao = repoDatabase.repoDao
        repoDao.deleteAll()

        val repos = mutableListOf<Repo?>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repoDao.getRepo(repo0.id).toList(repos)
        }
        assertEquals(0, repos.size)

        repoDao.insert(repo0)
        yield()
        assertEquals(repo0, repos[0])

        val repo0Mod = repo0.copy(name = "Modified")
        repoDao.insert(repo0Mod) // repo0 is replaced by repo0Mod in repo table.
        yield()
        assertEquals(repo0Mod, repos[1])

        repoDao.insert(repo1)
        repoDao.delete(repo0.id)
        yield()
        assertNull(repos[2])
        assertNull(repoDao.getRepo(repo0.id).first())
        assertEquals(repo1, repoDao.getRepo(repo1.id).first())

        repoDao.deleteAll()
        assertNull(repoDao.getRepo(repo0.id).first())
        assertNull(repoDao.getRepo(repo1.id).first())
    }

    @Test
    fun repoPageDaoTest() = runTest(testDispatcher) {
        val repoPageDao = repoDatabase.repoPageDao
        repoPageDao.deleteAll()

        repoPageDao.insert(listOf(repoPage0, repoPage1))
        assertEquals(repoPage0, repoPageDao.getRepoPage(repoPage0.repoId))
        assertEquals(repoPage1, repoPageDao.getRepoPage(repoPage1.repoId))

        repoPageDao.deleteAll()
        assertNull(repoPageDao.getRepoPage(repoPage0.repoId))
        assertNull(repoPageDao.getRepoPage(repoPage1.repoId))
    }

    @Test
    fun listRepoDaoTest() = runTest(testDispatcher) {
        val listRepoDao = repoDatabase.listRepoDao
        listRepoDao.deleteAll()

        val count = 5
        val repos = generateListRepos(count)
        val expectedRepos = repos.reversed()
        listRepoDao.insert(repos)
        val pagingSource = listRepoDao.getListRepos()
        val loadResult: PagingSourceLoadResult<Int, ListRepo> = pagingSource.load(
            PagingSourceLoadParamsRefresh(
                key = null,
                loadSize = count,
                placeholdersEnabled = false
            )
        )
        assertTrue(loadResult is PagingSourceLoadResultPage<Int, ListRepo>)
        assertEquals(expectedRepos, loadResult.data)
    }

    private fun generateListRepos(count: Int) = (1..count).map {
        ListRepo(
            id = it.toLong(),
            name = "name$it",
            fullName = "fullName$it",
            description = "description$it",
            stars = it,
            forks = it,
            language = "language$it"
        )
    }
}