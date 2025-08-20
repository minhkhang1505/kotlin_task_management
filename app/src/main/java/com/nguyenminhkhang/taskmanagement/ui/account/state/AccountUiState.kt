package com.nguyenminhkhang.taskmanagement.ui.account.state

data class AccountUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = true,
    val userName: String? = null,
    val userEmail: String? = null,
    val userAvatarUrl: String? = null,
    val isLogoutDialogVisible: Boolean = false
)
