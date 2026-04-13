package com.nguyenminhkhang.taskmanagement.ui.settings.settings

import com.nguyenminhkhang.shared.model.settings.ColorThemeOption
import com.nguyenminhkhang.shared.model.settings.FontStyleOption
import com.nguyenminhkhang.shared.model.settings.LanguageOption
import com.nguyenminhkhang.shared.model.settings.ThemeModeOption

sealed class AccountEvent {
    object SignOut : AccountEvent()
    object DismissLogoutDialog : AccountEvent()
    object ShowLogoutDialog : AccountEvent()
    object HideLogoutDialog : AccountEvent()

    data class ThemeModeChanged(val mode: ThemeModeOption) : AccountEvent()
    data class LanguageChanged( val language: LanguageOption) : AccountEvent()
    data class FontStyleChanged(val fontStyle: FontStyleOption) : AccountEvent()
    data class ColorThemeChanged(val colorTheme: ColorThemeOption) : AccountEvent()
    data class SaveThemeMode(val mode: ThemeModeOption) : AccountEvent()
}