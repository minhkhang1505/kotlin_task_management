package com.nguyenminhkhang.taskmanagement.ui.settings.account.state

import androidx.annotation.StringRes
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.settings.LanguageOption

data class SettingUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = true,
    val userName: String? = null,
    val userEmail: String? = null,
    val userAvatarUrl: String? = null,
    val isLogoutDialogVisible: Boolean = false,

    val languageRadioOption: String = LanguageOption.ENGLISH.code,
    val fontStyleOption: String = com.nguyenminhkhang.taskmanagement.ui.settings.FontStyleOption.DEFAULT.key,
    val colorThemeOption: String = com.nguyenminhkhang.taskmanagement.ui.settings.appearance.ColorThemeOption.PURPLE.key
)
