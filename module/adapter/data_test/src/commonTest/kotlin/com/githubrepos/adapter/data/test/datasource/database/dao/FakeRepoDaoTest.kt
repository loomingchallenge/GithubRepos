package com.githubrepos.adapter.data.test.datasource.database.dao

import app.cash.turbine.test
import com.githubrepos.core.test.repo1
import com.githubrepos.core.test.repo1New
import com.githubrepos.core.test.repo2
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FakeRepoDaoTest {

    private val fakeRepoDao = FakeRepoDao()

    @Test
    fun insert() = runTest {
        assertNull(fakeRepoDao.getRepo(repo1.id).first())
        assertNull(fakeRepoDao.getRepo(repo2.id).first())

        fakeRepoDao.insert(repo1)
        fakeRepoDao.insert(repo2)
        assertEquals(repo1, fakeRepoDao.getRepo(repo1.id).first())
        assertEquals(repo2, fakeRepoDao.getRepo(repo2.id).first())

        fakeRepoDao.insert(repo1New)
        assertEquals(repo1New, fakeRepoDao.getRepo(repo1.id).first())
        assertEquals(repo2, fakeRepoDao.getRepo(repo2.id).first())
    }

    @Test
    fun delete() = runTest {
        fakeRepoDao.insert(repo1)
        fakeRepoDao.insert(repo2)
        assertEquals(repo1, fakeRepoDao.getRepo(repo1.id).first())
        assertEquals(repo2, fakeRepoDao.getRepo(repo2.id).first())

        fakeRepoDao.delete(repo1.id)
        assertNull(fakeRepoDao.getRepo(repo1.id).first())
        assertEquals(repo2, fakeRepoDao.getRepo(repo2.id).first())

        fakeRepoDao.delete(repo2.id)
        assertNull(fakeRepoDao.getRepo(repo1.id).first())
        assertNull(fakeRepoDao.getRepo(repo2.id).first())
    }

    @Test
    fun deleteAll() = runTest {
        fakeRepoDao.insert(repo1)
        fakeRepoDao.insert(repo2)
        assertEquals(repo1, fakeRepoDao.getRepo(repo1.id).first())
        assertEquals(repo2, fakeRepoDao.getRepo(repo2.id).first())

        fakeRepoDao.deleteAll()
        assertNull(fakeRepoDao.getRepo(repo1.id).first())
        assertNull(fakeRepoDao.getRepo(repo2.id).first())
    }

    @Test
    fun getRepo() = runTest {
        fakeRepoDao.getRepo(repo1.id).test {
            assertEquals(null, awaitItem())
            fakeRepoDao.insert(repo1)
            assertEquals(repo1, awaitItem())
            fakeRepoDao.delete(repo1.id)
            assertNull(awaitItem())
            ensureAllEventsConsumed()
        }
    }
}