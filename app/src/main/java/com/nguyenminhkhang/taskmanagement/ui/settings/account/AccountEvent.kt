package com.nguyenminhkhang.taskmanagement.ui.settings.account

import androidx.annotation.StringRes
import com.nguyenminhkhang.taskmanagement.ui.settings.LanguageOption

sealed class AccountEvent {
    object SignOut : AccountEvent()
    object DismissLogoutDialog : AccountEvent()
    object ShowLogoutDialog : AccountEvent()
    object HideLogoutDialog : AccountEvent()

    data class ThemeModeChanged(@StringRes val mode: Int) : AccountEvent()
    data class LanguageChanged( val language: LanguageOption) : AccountEvent()
    data class SaveThemeMode(@StringRes val mode: Int) : AccountEvent()
}