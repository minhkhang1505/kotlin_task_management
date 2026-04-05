package com.nguyenminhkhang.taskmanagement.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.nguyenminhkhang.taskmanagement.ui.settings.LanguageOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class SettingsPreferenceData(
    val languageCode: String?,
    val themeModeRes: Int?,
    val fontStyleKey: String?,
    val colorThemeKey: String?
)

val DataStore<Preferences>.settingsFlow: Flow<SettingsPreferenceData>
    get() = data.map { preferences ->
        SettingsPreferenceData(
            languageCode = preferences[AccountDataStoreKeys.LANGUAGE_KEY],
            themeModeRes = preferences[AccountDataStoreKeys.THEME_MODE_KEY],
            fontStyleKey = preferences[AccountDataStoreKeys.FONT_STYLE_KEY],
            colorThemeKey = preferences[AccountDataStoreKeys.COLOR_THEME_KEY]
        )
    }

suspend fun DataStore<Preferences>.setLanguage(language: LanguageOption) {
    edit { preferences ->
        preferences[AccountDataStoreKeys.LANGUAGE_KEY] = language.code
    }
}

suspend fun DataStore<Preferences>.setThemeMode(themeMode: Int) {
    edit { preferences ->
        preferences[AccountDataStoreKeys.THEME_MODE_KEY] = themeMode
    }
}

suspend fun DataStore<Preferences>.setFontStyle(fontStyle: String) {
    edit { preferences ->
        preferences[AccountDataStoreKeys.FONT_STYLE_KEY] = fontStyle
    }
}

suspend fun DataStore<Preferences>.setColorTheme(colorTheme: String) {
    edit { preferences ->
        preferences[AccountDataStoreKeys.COLOR_THEME_KEY] = colorTheme
    }
}