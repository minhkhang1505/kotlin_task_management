package com.nguyenminhkhang.taskmanagement.ui.register

import androidx.compose.runtime.Composable
import com.nguyenminhkhang.taskmanagement.ui.register.state.RegisterState

@Composable
fun RegisterPage(
    registerState: RegisterState,
) {
    RegisterLayout(
        registerState = registerState,
    )
}