package com.githubrepos.core.test

import com.githubrepos.core.entity.ListRepo
import com.githubrepos.core.entity.Repo
import com.githubrepos.core.entity.RepoPage

val listRepo0 by lazy { ListRepo(
    0,
    "okhttp",
    "square/okhttp",
    "OkHttp is an efficient HTTP & HTTP/2 client for Android and Java applications.",
    44179,
    10311,
    "Kotlin"
) }

val repo0 by lazy { Repo(
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
) }

val repo1 by lazy { Repo(
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
) }

val repo48 by lazy { Repo(
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
) }

val repo49 by lazy { Repo(
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
) }

val repoPage0 by lazy { RepoPage(
    31792824,
    0
) }

val repoPage1 by lazy { RepoPage(
    29028775,
    1
) }