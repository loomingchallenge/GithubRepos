package com.githubrepos.framework.datasource.preferences.multiplatform_settings.di

import com.githubrepos.adapter.data.datasource.preferences.RepoPreferences
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.EncryptedPreferences
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.SecretKeyManager
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.id.repo.RepoPreferencesID
import com.githubrepos.framework.datasource.preferences.multiplatform_settings.preferences.RepoPreferencesImp
import com.russhwolf.settings.PreferencesSettings
import java.util.prefs.Preferences

fun createRepoPreferences(): RepoPreferences {
    val kyeManager = SecretKeyManager()
    val preferences = Preferences.userNodeForPackage(RepoPreferencesID::class.java)
    val encryptedPreferences = EncryptedPreferences(preferences, kyeManager.secretKey)
    return RepoPreferencesImp(PreferencesSettings(encryptedPreferences))
}














































