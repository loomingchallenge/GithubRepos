package com.githubrepos.adapter.presentation.viewmodel

import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.adapter.data.test.datasource.network.FakeNetworkStateProvider
import com.githubrepos.core.Lce
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.test.repo0
import com.githubrepos.core.test.repo0New
import com.githubrepos.core.test.repo0Old
import com.githubrepos.core.usecase.GetRepoUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetRepoUseCase(private val networkStateProvider: FakeNetworkStateProvider): GetRepoUseCase {

    override fun execute(id: Long, freshDataOnly: Boolean): Flow<Lce<out Repo>> {
        return if (networkStateProvider.hasNetworkConnection) {
            if (freshDataOnly) {
                flowOf(Lce.Loading, Lce.Content(repo0New.copy(id = id)))
            } else {
                flowOf(Lce.Content(repo0Old.copy(id = id)), Lce.Loading, Lce.Content(repo0.copy(id = id)))
            }
        } else {
            if (freshDataOnly) {
                flowOf(Lce.Loading, Lce.Error(NoInternetException()))
            } else {
                flowOf(Lce.Content(repo0Old.copy(id = id)), Lce.Loading, Lce.Error(NoInternetException()))
            }
        }
    }
}