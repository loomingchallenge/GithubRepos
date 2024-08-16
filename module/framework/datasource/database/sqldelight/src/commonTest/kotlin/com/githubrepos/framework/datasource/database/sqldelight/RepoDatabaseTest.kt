package com.githubrepos.framework.datasource.database.sqldelight

import app.cash.paging.PagingSourceLoadParamsRefresh
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultPage
import com.githubrepos.core.TestAppDispatchers
import com.githubrepos.core.entity.ListRepo
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.entity.repoExample
import com.githubrepos.core.entity.repoPageExample
import com.githubrepos.core.entity.repoPageExample2
import com.githubrepos.framework.datasource.database.sqldelight.di.createRepoDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


internal const val TEST_REPO_DATABASE_NAME = "TEST$REPO_DATABASE_NAME"

@Ignore
internal open class RepoDatabaseTest(databaseDriverFactory: DatabaseDriverFactory) {

    private val testDispatcher = StandardTestDispatcher()
    private val repoDatabase = createRepoDatabase(databaseDriverFactory, TestAppDispatchers(testDispatcher))

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    open fun repoDaoTest() = runTest(testDispatcher) {
        val repoDao = repoDatabase.repoDao
        repoDao.deleteAll()

        val repos = mutableListOf<Repo?>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repoDao.getRepo(repoExample.id).toList(repos)
        }
        assertEquals(0, repos.size)

        repoDao.insert(repoExample)
        yield()
        assertEquals(repoExample, repos[0])

        val repoExampleMod =   repoExample.copy(name = "Modified")
        repoDao.insert(repoExampleMod)
        yield()
        assertEquals(repoExampleMod, repos[1])

        repoDao.delete(repoExample.id)
        yield()
        assertNull(repos[2])
        assertNull(repoDao.getRepo(repoExample.id).first())

        repoDao.deleteAll()
        assertNull(repoDao.getRepo(repoExampleMod.id).first())
    }

    @Test
    open fun repoPageDaoTest() = runTest(testDispatcher) {
        val repoPageDao = repoDatabase.repoPageDao
        repoPageDao.deleteAll()

        repoPageDao.insert(listOf(repoPageExample, repoPageExample2))
        assertEquals(repoPageExample, repoPageDao.getRepoPage(repoPageExample.repoId))
        assertEquals(repoPageExample2, repoPageDao.getRepoPage(repoPageExample2.repoId))

        repoPageDao.deleteAll()
        assertNull(repoPageDao.getRepoPage(repoPageExample.repoId))
        assertNull(repoPageDao.getRepoPage(repoPageExample2.repoId))
    }

    @Test
    open fun listRepoDaoTest() = runTest(testDispatcher) {
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