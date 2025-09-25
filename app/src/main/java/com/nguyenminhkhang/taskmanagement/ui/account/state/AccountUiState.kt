package com.nguyenminhkhang.taskmanagement.ui.account.state

import androidx.annotation.StringRes
import com.nguyenminhkhang.taskmanagement.R

data class AccountUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = true,
    val userName: String? = null,
    val userEmail: String? = null,
    val userAvatarUrl: String? = null,
    val isLogoutDialogVisible: Boolean = false,

    @StringRes val languageRadioOption: List<Int> = listOf(
        R.string.language_english,
        R.string.language_vietnamese),
    @StringRes val selectedLanguage: Int = R.string.language_english,
)
