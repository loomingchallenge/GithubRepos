package com.githubrepos.adapter.data.test.datasource.database.dao

import app.cash.turbine.test
import com.githubrepos.core.test.repo0
import com.githubrepos.core.test.repo0New
import com.githubrepos.core.test.repo1
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FakeRepoDaoTest {

    private val fakeRepoDao = FakeRepoDao()

    @Test
    fun insert() = runTest {
        assertNull(fakeRepoDao.getRepo(repo0.id).first())
        assertNull(fakeRepoDao.getRepo(repo1.id).first())

        fakeRepoDao.insert(repo0)
        fakeRepoDao.insert(repo1)
        assertEquals(repo0, fakeRepoDao.getRepo(repo0.id).first())
        assertEquals(repo1, fakeRepoDao.getRepo(repo1.id).first())

        fakeRepoDao.insert(repo0New)
        assertEquals(repo0New, fakeRepoDao.getRepo(repo0.id).first())
        assertEquals(repo1, fakeRepoDao.getRepo(repo1.id).first())
    }

    @Test
    fun delete() = runTest {
        fakeRepoDao.insert(repo0)
        fakeRepoDao.insert(repo1)
        assertEquals(repo0, fakeRepoDao.getRepo(repo0.id).first())
        assertEquals(repo1, fakeRepoDao.getRepo(repo1.id).first())

        fakeRepoDao.delete(repo0.id)
        assertNull(fakeRepoDao.getRepo(repo0.id).first())
        assertEquals(repo1, fakeRepoDao.getRepo(repo1.id).first())

        fakeRepoDao.delete(repo1.id)
        assertNull(fakeRepoDao.getRepo(repo0.id).first())
        assertNull(fakeRepoDao.getRepo(repo1.id).first())
    }

    @Test
    fun deleteAll() = runTest {
        fakeRepoDao.insert(repo0)
        fakeRepoDao.insert(repo1)
        assertEquals(repo0, fakeRepoDao.getRepo(repo0.id).first())
        assertEquals(repo1, fakeRepoDao.getRepo(repo1.id).first())

        fakeRepoDao.deleteAll()
        assertNull(fakeRepoDao.getRepo(repo0.id).first())
        assertNull(fakeRepoDao.getRepo(repo1.id).first())
    }

    @Test
    fun getRepo() = runTest {
        fakeRepoDao.getRepo(repo0.id).test {
            assertEquals(null, awaitItem())
            fakeRepoDao.insert(repo0)
            assertEquals(repo0, awaitItem())
            fakeRepoDao.delete(repo0.id)
            assertNull(awaitItem())
            ensureAllEventsConsumed()
        }
    }
}