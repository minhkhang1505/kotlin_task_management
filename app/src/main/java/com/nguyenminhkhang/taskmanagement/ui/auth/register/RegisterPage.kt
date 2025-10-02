package com.nguyenminhkhang.taskmanagement.ui.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun RegisterPage(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val registerState by viewModel.registerState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToLogin -> {
                    // Tùy chọn: Gửi một thông báo về trang Login
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("register_success_message", "Registration successful! Please log in.")

                    // Quay lại màn hình trước đó (chính là Login)
                    navController.popBackStack()
                }
            }
        }
    }

    RegisterLayout(
        registerUiState = registerState,
        navController = navController,
        onEvent = viewModel::onEvent,
    )
}