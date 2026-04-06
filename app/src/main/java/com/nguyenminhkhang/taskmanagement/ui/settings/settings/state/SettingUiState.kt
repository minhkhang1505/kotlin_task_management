package com.nguyenminhkhang.taskmanagement.ui.settings.settings.state

import com.nguyenminhkhang.taskmanagement.ui.settings.FontStyleOption
import com.nguyenminhkhang.taskmanagement.ui.settings.LanguageOption
import com.nguyenminhkhang.taskmanagement.ui.settings.appearance.ColorThemeOption

data class SettingUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = true,
    val userName: String? = null,
    val userEmail: String? = null,
    val userAvatarUrl: String? = null,
    val isLogoutDialogVisible: Boolean = false,

    val languageRadioOption: String = LanguageOption.ENGLISH.code,
    val fontStyleOption: String = FontStyleOption.DEFAULT.key,
    val colorThemeOption: String = ColorThemeOption.PURPLE.key
)
