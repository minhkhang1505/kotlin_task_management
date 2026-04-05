package com.nguyenminhkhang.taskmanagement.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

private const val SETTINGS_PREFERENCES_NAME = "settings_preferences"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_PREFERENCES_NAME)

object AccountDataStoreKeys {
    val THEME_MODE_KEY = intPreferencesKey("theme_mode_key")
    val LANGUAGE_KEY = stringPreferencesKey("language_key")
    val FONT_STYLE_KEY = stringPreferencesKey("font_style_key")
    val COLOR_THEME_KEY = stringPreferencesKey("color_theme_key")
}