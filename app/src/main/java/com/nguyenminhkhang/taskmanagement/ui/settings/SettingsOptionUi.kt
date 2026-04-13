package com.nguyenminhkhang.taskmanagement.ui.settings

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.nguyenminhkhang.shared.model.settings.ColorThemeOption
import com.nguyenminhkhang.shared.model.settings.FontStyleOption
import com.nguyenminhkhang.shared.model.settings.LanguageOption
import com.nguyenminhkhang.shared.model.settings.ThemeModeOption
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.theme.Blue40
import com.nguyenminhkhang.taskmanagement.ui.theme.Green40
import com.nguyenminhkhang.taskmanagement.ui.theme.Orange40
import com.nguyenminhkhang.taskmanagement.ui.theme.Purple40
import com.nguyenminhkhang.taskmanagement.ui.theme.Red40

@get:StringRes
val LanguageOption.labelRes: Int
    get() = when (this) {
        LanguageOption.ENGLISH -> R.string.language_english
        LanguageOption.VIETNAMESE -> R.string.language_vietnamese
    }

@get:StringRes
val FontStyleOption.labelRes: Int
    get() = when (this) {
        FontStyleOption.DEFAULT -> R.string.font_default
        FontStyleOption.SERIF -> R.string.font_serif
        FontStyleOption.SANS_SERIF -> R.string.font_sans_serif
        FontStyleOption.MONOSPACE -> R.string.font_monospace
        FontStyleOption.CURSIVE -> R.string.font_cursive
    }

val FontStyleOption.fontFamily: FontFamily
    get() = when (this) {
        FontStyleOption.DEFAULT -> FontFamily.Default
        FontStyleOption.SERIF -> FontFamily.Serif
        FontStyleOption.SANS_SERIF -> FontFamily.SansSerif
        FontStyleOption.MONOSPACE -> FontFamily.Monospace
        FontStyleOption.CURSIVE -> FontFamily.Cursive
    }

@get:StringRes
val ThemeModeOption.labelRes: Int
    get() = when (this) {
        ThemeModeOption.LIGHT -> R.string.light_mode
        ThemeModeOption.DARK -> R.string.dark_mode
        ThemeModeOption.SYSTEM -> R.string.system_mode
    }

@get:StringRes
val ColorThemeOption.labelRes: Int
    get() = when (this) {
        ColorThemeOption.PURPLE -> R.string.color_theme_purple
        ColorThemeOption.RED -> R.string.color_theme_red
        ColorThemeOption.GREEN -> R.string.color_theme_green
        ColorThemeOption.BLUE -> R.string.color_theme_blue
        ColorThemeOption.ORANGE -> R.string.color_theme_orange
    }

val ColorThemeOption.primaryColorPreview: Color
    get() = when (this) {
        ColorThemeOption.PURPLE -> Purple40
        ColorThemeOption.RED -> Red40
        ColorThemeOption.GREEN -> Green40
        ColorThemeOption.BLUE -> Blue40
        ColorThemeOption.ORANGE -> Orange40
    }