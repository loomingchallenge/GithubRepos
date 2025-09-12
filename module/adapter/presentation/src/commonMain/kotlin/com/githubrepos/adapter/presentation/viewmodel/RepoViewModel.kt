package com.githubrepos.adapter.presentation.viewmodel

import com.githubrepos.core.LceFlows
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.saveToLceFlows
import com.githubrepos.core.usecase.GetRepoUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn

interface RepoViewModel {
    val title: String
    val repoLceFlows: LceFlows<Repo>
    fun loadRepo(freshDataOnly: Boolean)
}

internal class RepoViewModelImp (
    private val repoID: Long,
    override val title: String,
    private val scope: CoroutineScope,
    private val getRepoUseCase: GetRepoUseCase
) : RepoViewModel {

    override val repoLceFlows = LceFlows<Repo>()

    private var repoJob: Job? = null

    init {
        loadRepo(false)
    }

    override fun loadRepo(freshDataOnly: Boolean) {
        repoJob?.cancel()
        repoLceFlows.loading.value = false
        repoLceFlows.clearError()
        repoJob = getRepoUseCase.execute(repoID, freshDataOnly)
            .saveToLceFlows(repoLceFlows)
            .launchIn(scope)
    }
}
