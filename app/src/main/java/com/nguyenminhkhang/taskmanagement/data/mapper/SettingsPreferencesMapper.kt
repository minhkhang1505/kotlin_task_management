package com.nguyenminhkhang.taskmanagement.data.mapper

import com.nguyenminhkhang.taskmanagement.data.datastore.SettingsPreferenceData
import com.nguyenminhkhang.taskmanagement.domain.model.SettingsPreferences

fun SettingsPreferenceData.toDomain(): SettingsPreferences = SettingsPreferences(
    languageCode = languageCode ?: SettingsPreferences().languageCode,
    themeModeKey = themeModeKey ?: SettingsPreferences().themeModeKey,
    fontStyleKey = fontStyleKey ?: SettingsPreferences().fontStyleKey,
    colorThemeKey = colorThemeKey ?: SettingsPreferences().colorThemeKey,
)