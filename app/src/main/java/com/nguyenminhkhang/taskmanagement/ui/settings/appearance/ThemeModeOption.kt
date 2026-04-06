package com.nguyenminhkhang.taskmanagement.ui.settings.appearance

import androidx.annotation.StringRes
import com.nguyenminhkhang.taskmanagement.R

enum class ThemeModeOption(
    val key: String,
    @StringRes val labelRes: Int
) {
    LIGHT("light", R.string.light_mode),
    DARK("dark", R.string.dark_mode),
    SYSTEM("system", R.string.system_mode);

    companion object {
        fun fromStorage(value: String?): ThemeModeOption = entries.firstOrNull { it.key == value } ?: LIGHT
    }
}