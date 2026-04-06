package com.nguyenminhkhang.taskmanagement.ui.settings.settings

import com.nguyenminhkhang.taskmanagement.ui.settings.FontStyleOption
import com.nguyenminhkhang.taskmanagement.ui.settings.LanguageOption
import com.nguyenminhkhang.taskmanagement.ui.settings.appearance.ThemeModeOption

sealed class AccountEvent {
    object SignOut : AccountEvent()
    object DismissLogoutDialog : AccountEvent()
    object ShowLogoutDialog : AccountEvent()
    object HideLogoutDialog : AccountEvent()

    data class ThemeModeChanged(val mode: ThemeModeOption) : AccountEvent()
    data class LanguageChanged( val language: LanguageOption) : AccountEvent()
    data class FontStyleChanged(val fontStyle: FontStyleOption) : AccountEvent()
    data class ColorThemeChanged(val colorTheme: com.nguyenminhkhang.taskmanagement.ui.settings.appearance.ColorThemeOption) : AccountEvent()
    data class SaveThemeMode(val mode: ThemeModeOption) : AccountEvent()
}