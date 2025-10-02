package com.nguyenminhkhang.taskmanagement.ui.auth.signin.state

data class SignInState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val isPasswordVisible: Boolean = false,
)
