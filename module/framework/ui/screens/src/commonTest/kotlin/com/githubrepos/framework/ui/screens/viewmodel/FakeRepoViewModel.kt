package com.githubrepos.framework.ui.screens.viewmodel

import com.githubrepos.adapter.presentation.viewmodel.RepoViewModel
import com.githubrepos.core.LceFlows
import com.githubrepos.core.entity.Repo

class FakeRepoViewModel(
    override val title: String = "Fake Repo"
) : RepoViewModel {

    override val repoLceFlows = LceFlows<Repo>()

    override fun loadRepo(freshDataOnly: Boolean) {
        wasLoadRepoCalled = true
        this.freshDataOnly = freshDataOnly
    }

    var wasLoadRepoCalled: Boolean = false
        private set

    var freshDataOnly: Boolean? = null
        private set

    fun emit(loading: Boolean = false, content: Repo? = null, error: Throwable? = null) {
        repoLceFlows.loading.value = loading
        repoLceFlows.content.value = content
        repoLceFlows.error.value = error
    }
}