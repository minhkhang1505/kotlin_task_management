package com.nguyenminhkhang.taskmanagement.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.nguyenminhkhang.taskmanagement.datastore.AccountDataStoreKeys.LANGUAGE_KEY
import com.nguyenminhkhang.taskmanagement.datastore.AccountDataStoreKeys.THEME_MODE_KEY
import com.nguyenminhkhang.taskmanagement.ui.settings.LanguageOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class SettingsPreferenceData(
    val languageCode: String?,
    val themeModeRes: Int?
)

val DataStore<Preferences>.settingsFlow: Flow<SettingsPreferenceData>
    get() = data.map { preferences ->
        SettingsPreferenceData(
            languageCode = preferences[LANGUAGE_KEY],
            themeModeRes = preferences[THEME_MODE_KEY]
        )
    }

suspend fun DataStore<Preferences>.setLanguage(language: LanguageOption) {
    edit { preferences ->
        preferences[LANGUAGE_KEY] = language.code
    }
}

suspend fun DataStore<Preferences>.setThemeMode(themeMode: Int) {
    edit { preferences ->
        preferences[THEME_MODE_KEY] = themeMode
    }
}