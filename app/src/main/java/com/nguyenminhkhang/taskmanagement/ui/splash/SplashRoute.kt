package com.nguyenminhkhang.taskmanagement.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashRoute(
	onNavigateToHome: () -> Unit,
	onNavigateToSignIn: () -> Unit,
) {
	val viewModel: SplashScreenViewModel = koinViewModel()
	val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

	LaunchedEffect(uiState.isLoggedIn) {
		when (uiState.isLoggedIn) {
			true -> onNavigateToHome()
			false -> onNavigateToSignIn()
			null -> Unit
		}
	}

	SplashScreen(isLoading = uiState.isLoading)

}