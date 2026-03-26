package com.nguyenminhkhang.taskmanagement.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nguyenminhkhang.taskmanagement.data.datastore.setLanguage
import com.nguyenminhkhang.taskmanagement.data.datastore.setThemeMode
import com.nguyenminhkhang.taskmanagement.data.datastore.settingsFlow
import com.nguyenminhkhang.taskmanagement.domain.repository.SettingsRepository
import com.nguyenminhkhang.taskmanagement.ui.settings.LanguageOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import com.nguyenminhkhang.taskmanagement.data.datastore.SettingsPreferenceData
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    companion object {
        private const val TAG = "SettingsRepository"
    }

    override val settingsFlow: Flow<SettingsPreferenceData>
        get() = dataStore.settingsFlow.onEach { prefs ->
            Timber.tag(TAG).d(
                "settingsFlow read - languageCode=%s, themeModeRes=%s",
                prefs.languageCode,
                prefs.themeModeRes
            )
        }

    override suspend fun setLanguage(language: LanguageOption) {
        Timber.tag(TAG).d("setLanguage() - writing languageCode=%s", language.code)
        dataStore.setLanguage(language)
    }

    override suspend fun setThemeMode(themeMode: Int) {
        Timber.tag(TAG).d("setThemeMode() - writing themeModeRes=%s", themeMode)
        dataStore.setThemeMode(themeMode)
    }
}