package com.nguyenminhkhang.taskmanagement.ui.settings.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingRoute(
    settingViewModel: SettingViewModel,
    onNavigateToLanguage: () -> Unit,
    onNavigateToTheme: () -> Unit,
    onNavigateToFontStyle: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val accountUiState by settingViewModel.settingsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        settingViewModel.logoutEvent.collect {
            onNavigateToLogin()
        }
    }

    SettingScreen(
        settingUiState = accountUiState,
        onEvent = settingViewModel::onEvent,
        onNavigateToTheme = onNavigateToTheme,
        onNavigateToLanguage = onNavigateToLanguage,
        onNavigateToFontStyle = onNavigateToFontStyle,
        onScreenShow = settingViewModel::onScreenShow
    )
}