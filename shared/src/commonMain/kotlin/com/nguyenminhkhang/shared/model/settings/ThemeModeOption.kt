package com.nguyenminhkhang.shared.model.settings

enum class ThemeModeOption(val key: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system");

    companion object {
        fun fromStorage(value: String?): ThemeModeOption =
            entries.firstOrNull { it.key == value } ?: LIGHT
    }
}