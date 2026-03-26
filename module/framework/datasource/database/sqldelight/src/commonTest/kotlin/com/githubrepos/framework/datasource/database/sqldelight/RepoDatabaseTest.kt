package com.githubrepos.framework.datasource.database.sqldelight

import androidx.paging.PagingSource
import com.githubrepos.adapter.data.datasource.database.RepoDatabase
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.test.TestAppDispatchers
import com.githubrepos.core.test.repo1
import com.githubrepos.core.test.repo2
import com.githubrepos.core.test.repoPage1
import com.githubrepos.core.test.repoPage2
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
            repoDao.getRepo(repo1.id).toList(repos)
        }
        assertEquals(0, repos.size)

        repoDao.insert(repo1)
        yield()
        assertEquals(repo1, repos[0])

        val repo0Mod = repo1.copy(name = "Modified")
        repoDao.insert(repo0Mod) // repo0 is replaced by repo0Mod in repo table.
        yield()
        assertEquals(repo0Mod, repos[1])

        repoDao.insert(repo2)
        repoDao.delete(repo1.id)
        yield()
        assertNull(repos[2])
        assertNull(repoDao.getRepo(repo1.id).first())
        assertEquals(repo2, repoDao.getRepo(repo2.id).first())

        repoDao.deleteAll()
        assertNull(repoDao.getRepo(repo1.id).first())
        assertNull(repoDao.getRepo(repo2.id).first())
    }

    @Test
    fun repoPageDaoTest() = runTest(testDispatcher) {
        val repoPageDao = repoDatabase.repoPageDao
        repoPageDao.deleteAll()

        repoPageDao.insert(listOf(repoPage1, repoPage2))
        assertEquals(repoPage1, repoPageDao.getRepoPage(repoPage1.repoId))
        assertEquals(repoPage2, repoPageDao.getRepoPage(repoPage2.repoId))

        repoPageDao.deleteAll()
        assertNull(repoPageDao.getRepoPage(repoPage1.repoId))
        assertNull(repoPageDao.getRepoPage(repoPage2.repoId))
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
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = count,
                placeholdersEnabled = false
            )
        )
        assertTrue(loadResult is PagingSource.LoadResult.Page)
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