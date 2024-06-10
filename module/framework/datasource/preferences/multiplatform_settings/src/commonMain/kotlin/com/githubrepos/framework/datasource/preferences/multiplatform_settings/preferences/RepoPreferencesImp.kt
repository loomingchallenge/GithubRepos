package com.githubrepos.framework.datasource.preferences.multiplatform_settings.preferences

import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.russhwolf.settings.Settings
import com.russhwolf.settings.nullableString

internal const val repoPreferencesName = "RepoPreferences"

internal class RepoPreferencesImp(settings: Settings): RepoPreferences {

    override var query: String? by settings.nullableString()
}
