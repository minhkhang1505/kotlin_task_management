package com.nguyenminhkhang.taskmanagement.ui.login

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun LoginPage(
    loginState: LoginState,
    navController: NavController
) {
    LoginLayout(
        loginState = loginState,
        navController = navController
    )
}