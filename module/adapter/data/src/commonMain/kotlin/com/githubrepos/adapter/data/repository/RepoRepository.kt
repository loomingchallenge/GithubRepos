package com.githubrepos.adapter.data.repository

import com.githubrepos.adapter.data.convertor.fromDBToDomain
import com.githubrepos.adapter.data.convertor.fromNetworkToDB
import com.githubrepos.adapter.data.datasource.database.dao.RepoDao
import com.githubrepos.adapter.data.datasource.network.NetworkStateProvider
import com.githubrepos.adapter.data.datasource.network.service.GithubService
import com.githubrepos.adapter.data.toLce
import com.githubrepos.core.Lce
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest

typealias RepoStore = Store<Long, Repo>

internal class RepoRepositoryImp(
    private val networkStateProvider: NetworkStateProvider,
    private val githubService: GithubService,
    private val repoDao: RepoDao
): RepoRepository, RepoStore by create(githubService, repoDao) {

    companion object {

        private fun create(githubService: GithubService, repoDao: RepoDao): RepoStore {
            return StoreBuilder.from<Long, Repo, Repo>( // key, network, db
                fetcher = Fetcher.of { id ->
                    githubService.getRepo(id)
                },
                sourceOfTruth = SourceOfTruth.of(
                    reader = { id ->
                        repoDao.getRepo(id)
                            .map { it?.fromDBToDomain() } // to domain
                            .distinctUntilChanged()
                    },
                    writer = { id, repo -> // network
                        repoDao.insert(repo.fromNetworkToDB())
                    },
                    delete = { id ->
                        repoDao.delete(id)
                    },
                    deleteAll = { repoDao.deleteAll() },
                )
            ).build()
        }
    }

    override fun getRepo(id: Long, freshDataOnly: Boolean): Flow<Lce<out Repo>> {
        val request = if (freshDataOnly) {
            StoreReadRequest.fresh(id)
        } else {
            StoreReadRequest.cached(id, networkStateProvider.hasNetworkConnection())
        }
        return this.stream(request).toLce()
    }
}
