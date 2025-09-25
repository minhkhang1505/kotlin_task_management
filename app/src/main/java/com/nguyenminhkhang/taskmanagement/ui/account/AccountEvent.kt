package com.nguyenminhkhang.taskmanagement.ui.account

import androidx.annotation.StringRes

sealed class AccountEvent {
    object SignOut : AccountEvent()
    object DismissLogoutDialog : AccountEvent()
    object ShowLogoutDialog : AccountEvent()
    object HideLogoutDialog : AccountEvent()

    data class ThemeModeChanged(@StringRes val mode: Int) : AccountEvent()
    data class LanguageChanged(@StringRes val language: Int) : AccountEvent()
    data class SaveThemeMode(@StringRes val mode: Int) : AccountEvent()
}