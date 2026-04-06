package com.nguyenminhkhang.taskmanagement.domain.model

data class SettingsPreferences(
    val languageCode: String = "en",
    val themeModeKey: String = "light",
    val fontStyleKey: String = "default",
    val colorThemeKey: String = "purple"
)