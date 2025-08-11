package com.nguyenminhkhang.taskmanagement.ui.register

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.ui.register.state.RegisterState

@Composable
fun RegisterPage(
    registerState: RegisterState,
    navController: NavController
) {
    RegisterLayout(
        registerState = registerState,
        navController = navController
    )
}