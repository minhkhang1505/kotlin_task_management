package com.nguyenminhkhang.taskmanagement.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey

private const val SETTINGS_PREFERENCES_NAME = "settings_preferences"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_PREFERENCES_NAME)

object AccountDataStoreKeys {
    val THEME_MODE_KEY = intPreferencesKey("theme_mode_key")
    val LANGUAGE_KEY = intPreferencesKey("language_key")
}