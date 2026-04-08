package com.nguyenminhkhang.taskmanagement.domain.repository

import com.nguyeminhkhang.shared.model.SettingsPreferences
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<SettingsPreferences>
    suspend fun setLanguage(languageCode: String)
    suspend fun setThemeMode(themeModeKey: String)
    suspend fun setFontStyle(fontStyle: String)
    suspend fun setColorTheme(colorTheme: String)
}