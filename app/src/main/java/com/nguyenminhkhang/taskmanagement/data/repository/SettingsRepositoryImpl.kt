package com.nguyenminhkhang.taskmanagement.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nguyenminhkhang.taskmanagement.data.datastore.setColorTheme
import com.nguyenminhkhang.taskmanagement.data.datastore.setFontStyle
import com.nguyenminhkhang.taskmanagement.data.datastore.setLanguage
import com.nguyenminhkhang.taskmanagement.data.datastore.setThemeMode
import com.nguyenminhkhang.taskmanagement.data.datastore.settingsFlow
import com.nguyenminhkhang.taskmanagement.data.mapper.toDomain
import com.nguyeminhkhang.shared.model.SettingsPreferences
import com.nguyenminhkhang.taskmanagement.domain.repository.SettingsRepository
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@Singleton
class SettingsRepositoryImpl (
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    companion object {
        private const val TAG = "SettingsRepository"
    }

    override val settingsFlow: Flow<SettingsPreferences>
        get() = dataStore.settingsFlow
            .onEach { prefs ->
                Timber.tag(TAG).d(
                    "settingsFlow read - languageCode=%s, themeModeKey=%s, fontStyleKey=%s, colorThemeKey=%s",
                    prefs.languageCode,
                    prefs.themeModeKey,
                    prefs.fontStyleKey,
                    prefs.colorThemeKey
                )
            }
            .map { prefs -> prefs.toDomain() }

    override suspend fun setLanguage(languageCode: String) {
        Timber.tag(TAG).d("setLanguage() - writing languageCode=%s", languageCode)
        dataStore.setLanguage(languageCode)
    }

    override suspend fun setThemeMode(themeModeKey: String) {
        Timber.tag(TAG).d("setThemeMode() - writing themeModeKey=%s", themeModeKey)
        dataStore.setThemeMode(themeModeKey)
    }

    override suspend fun setFontStyle(fontStyle: String) {
        Timber.tag(TAG).d("setFontStyle() - writing fontStyleKey=%s", fontStyle)
        dataStore.setFontStyle(fontStyle)
    }

    override suspend fun setColorTheme(colorTheme: String) {
        Timber.tag(TAG).d("setColorTheme() - writing colorThemeKey=%s", colorTheme)
        dataStore.setColorTheme(colorTheme)
    }
}