package com.githubrepos.framework.datasource.network.demo_api

import com.githubrepos.adapter.data.datasource.network.NoInternetException
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.entity.toListRepo
import com.githubrepos.framework.datasource.network.demo_api.di.createGithubService
import com.githubrepos.framework.datasource.network.demo_api.di.createNetworkServiceProvider
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GithubServiceTest {

    private val totalCount = 50

    private val expRepo0 = Repo(
        31792824,
        "flutter",
        "flutter/flutter",
        "Flutter makes it easy and fast to build beautiful apps for mobile and beyond",
        "https://github.com/flutter/flutter",
        "https://flutter.dev",
        "https://github.com/flutter/flutter.git",
        "https://api.github.com/repos/flutter/flutter/deployments",
        159256,
        26722,
        "Dart"
    )

    private val expRepo1 = Repo(
        29028775,
        "react-native",
        "facebook/react-native",
        "A framework for building native applications using React",
        "https://github.com/facebook/react-native",
        "https://reactnative.dev",
        "https://github.com/facebook/react-native.git",
        "https://api.github.com/repos/facebook/react-native/deployments",
        113772,
        24144,
        "C++"
    )

    private val expRepo48 = Repo(
        90792131,
        "architecture-components-samples",
        "android/architecture-components-samples",
        "Samples for Android Architecture Components. ",
        "https://github.com/android/architecture-components-samples",
        "https://d.android.com/arch",
        "https://github.com/android/architecture-components-samples.git",
        "https://api.github.com/repos/android/architecture-components-samples/deployments",
        23136,
        8340,
        "Kotlin"
    )

    private val expRepo49 = Repo(
        14964475,
        "Avalonia",
        "AvaloniaUI/Avalonia",
        "Develop Desktop, Embedded, Mobile and WebAssembly apps with C# and XAML. The most popular .NET Foundation community project.",
        "https://github.com/AvaloniaUI/Avalonia",
        "https://avaloniaui.net",
        "https://github.com/AvaloniaUI/Avalonia.git",
        "https://api.github.com/repos/AvaloniaUI/Avalonia/deployments",
        22644,
        2013,
        "C#"
    )

    private val networkStateProvider = NetworkStateProviderImp()
    private val networkServiceProvider = createNetworkServiceProvider(networkStateProvider)
    private val githubService = createGithubService(networkServiceProvider)

    @Test
    fun getRepo() = runTest {
        networkStateProvider.hasNetworkConnection = true

        var repo = githubService.getRepo(expRepo0.id)
        assertEquals(expRepo0, repo)

        repo = githubService.getRepo(expRepo1.id)
        assertEquals(expRepo1, repo)

        repo = githubService.getRepo(expRepo48.id)
        assertEquals(expRepo48, repo)

        repo = githubService.getRepo(expRepo49.id)
        assertEquals(expRepo49, repo)

        assertFailsWith<IllegalArgumentException> {
            githubService.getRepo(-1)
        }

        networkStateProvider.hasNetworkConnection = false
        assertFailsWith<NoInternetException> {
            githubService.getRepo(expRepo0.id)
        }
    }

    @Test
    fun searchRepos() = runTest {
        networkStateProvider.hasNetworkConnection = true

        var response = githubService.searchRepos("", 1, 2)
        assertEquals(totalCount, response.total)
        assertEquals(2, response.items.size)
        assertEquals(expRepo0.toListRepo(), response.items[0])
        assertEquals(expRepo1.toListRepo(), response.items[1])

        response = githubService.searchRepos("", 25, 2)
        assertEquals(totalCount, response.total)
        assertEquals(2, response.items.size)
        assertEquals(expRepo48.toListRepo(), response.items[0])
        assertEquals(expRepo49.toListRepo(), response.items[1])

        networkStateProvider.hasNetworkConnection = false
        assertFailsWith<NoInternetException> {
            githubService.searchRepos("", 1, 2)
        }
    }
}