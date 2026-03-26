package com.githubrepos.core.test

import com.githubrepos.core.entity.ListRepo
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.entity.RepoPage
import kotlin.random.Random

val repo1 by lazy { Repo(
    31792824,
    "flutter",
    "1.flutter/flutter",
    "Flutter makes it easy and fast to build beautiful apps for mobile and beyond",
    "https://github.com/flutter/flutter",
    "https://flutter.dev",
    "https://github.com/flutter/flutter.git",
    "https://api.github.com/repos/flutter/flutter/deployments",
    159256,
    26722,
    "Dart"
) }

val repo1Old by lazy { Repo(
    31792824,
    "flutter",
    "1.flutter/flutter",
    "Flutter makes it easy and fast to build beautiful apps for mobile and beyond",
    "https://github.com/flutter/flutter",
    "https://flutter.dev",
    "https://github.com/flutter/flutter.git",
    "https://api.github.com/repos/flutter/flutter/deployments",
    146377,
    24647,
    "Dart"
) }

val repo1New by lazy { Repo(
    31792824,
    "flutter",
    "1.flutter/flutter",
    "Flutter makes it easy and fast to build beautiful apps for mobile and beyond",
    "https://github.com/flutter/flutter",
    "https://flutter.dev",
    "https://github.com/flutter/flutter.git",
    "https://api.github.com/repos/flutter/flutter/deployments",
    174586,
    28749,
    "Dart"
) }

val repo2 by lazy { Repo(
    29028775,
    "react-native",
    "2.facebook/react-native",
    "A framework for building native applications using React",
    "https://github.com/facebook/react-native",
    "https://reactnative.dev",
    "https://github.com/facebook/react-native.git",
    "https://api.github.com/repos/facebook/react-native/deployments",
    113772,
    24144,
    "C++"
) }

val repo49 by lazy { Repo(
    90792131,
    "architecture-components-samples",
    "49.android/architecture-components-samples",
    "Samples for Android Architecture Components. ",
    "https://github.com/android/architecture-components-samples",
    "https://d.android.com/arch",
    "https://github.com/android/architecture-components-samples.git",
    "https://api.github.com/repos/android/architecture-components-samples/deployments",
    23136,
    8340,
    "Kotlin"
) }

val repo50 by lazy { Repo(
    14964475,
    "Avalonia",
    "50.AvaloniaUI/Avalonia",
    "Develop Desktop, Embedded, Mobile and WebAssembly apps with C# and XAML. The most popular .NET Foundation community project.",
    "https://github.com/AvaloniaUI/Avalonia",
    "https://avaloniaui.net",
    "https://github.com/AvaloniaUI/Avalonia.git",
    "https://api.github.com/repos/AvaloniaUI/Avalonia/deployments",
    22644,
    2013,
    "C#"
) }

val repoPage1 by lazy { RepoPage(
    31792824,
    1
) }

val repoPage2 by lazy { RepoPage(
    29028775,
    2
) }

val listRepo1 by lazy { ListRepo(
    31792824,
    "flutter",
    "1.flutter/flutter",
    "Flutter makes it easy and fast to build beautiful apps for mobile and beyond",
    159256,
    26722,
    "Dart"
) }

fun generateListRepos(count: Int) = (1..count).map {
    ListRepo(
        id = it.toLong(),
        name = "name$it",
        fullName = "fullName$it",
        description = "description$it " + randomParagraph(),
        stars = 10000000 - it,
        forks = it,
        language = "language$it"
    )
}

private fun randomParagraph(): String {
    val targetLength = Random.nextInt(0, 200)
    val lorem = "Lorem ipsum dolor sit amet consectetur adipiscing budo "
    val builder = StringBuilder()

    while (builder.length < targetLength) {
        builder.append(lorem)
    }

    return builder.toString().take(targetLength)
}