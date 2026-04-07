package com.githubrepos.framework.datasource.preferences.multiplatform_settings.di

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.preferences.RepoPreferencesImp
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.preferences.REPO_PREFERENCES_NAME
import com.russhwolf.settings.SharedPreferencesSettings

fun createRepoPreferences(context: Context, repoPreferencesName: String = REPO_PREFERENCES_NAME): RepoPreferences {
    val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        repoPreferencesName,
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    return RepoPreferencesImp(SharedPreferencesSettings(sharedPreferences))
}

