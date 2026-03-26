package com.nguyenminhkhang.taskmanagement.domain.repository

import com.nguyenminhkhang.taskmanagement.data.datastore.SettingsPreferenceData
import com.nguyenminhkhang.taskmanagement.ui.settings.LanguageOption
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<SettingsPreferenceData>
    suspend fun setLanguage(language: LanguageOption)
    suspend fun setThemeMode(themeMode: Int)
}