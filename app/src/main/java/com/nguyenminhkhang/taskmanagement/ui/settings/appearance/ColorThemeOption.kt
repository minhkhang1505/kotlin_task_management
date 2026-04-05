package com.nguyenminhkhang.taskmanagement.ui.settings.appearance

import androidx.compose.ui.graphics.Color
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.theme.Purple40
import com.nguyenminhkhang.taskmanagement.ui.theme.Red40
import com.nguyenminhkhang.taskmanagement.ui.theme.Green40
import com.nguyenminhkhang.taskmanagement.ui.theme.Blue40
import com.nguyenminhkhang.taskmanagement.ui.theme.Orange40

enum class ColorThemeOption(
    val key: String,
    val labelRes: Int,
    val primaryColorPreview: Color
) {
    PURPLE("purple", R.string.color_theme_purple, Purple40),
    RED("red", R.string.color_theme_red, Red40),
    GREEN("green", R.string.color_theme_green, Green40),
    BLUE("blue", R.string.color_theme_blue, Blue40),
    ORANGE("orange", R.string.color_theme_orange, Orange40);

    companion object {
        fun fromStorage(value: String?): ColorThemeOption {
            if (value.isNullOrBlank()) return PURPLE
            return entries.firstOrNull { it.key == value }
                ?: entries.firstOrNull { it.name == value }
                ?: PURPLE
        }
    }
}
