package com.nguyenminhkhang.taskmanagement.ui.settings.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingRoute(
    onNavigateToLanguage: () -> Unit,
    onNavigateToTheme: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val settingViewModel: SettingViewModel = hiltViewModel()
    val accountUiState by settingViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        settingViewModel.logoutEvent.collect {
            onNavigateToLogin()
        }
    }

    SettingScreen(
        accountUiState = accountUiState,
        onEvent = settingViewModel::onEvent,
        onNavigateToTheme = onNavigateToTheme,
        onNavigateToLanguage = onNavigateToLanguage
    )
}