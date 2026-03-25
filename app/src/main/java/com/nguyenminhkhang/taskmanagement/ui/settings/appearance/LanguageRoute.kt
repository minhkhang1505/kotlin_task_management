package com.nguyenminhkhang.taskmanagement.ui.settings.appearance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.nguyenminhkhang.taskmanagement.ui.settings.account.SettingViewModel

@Composable
fun LanguageRoute(
    settingViewModel: SettingViewModel,
    onPopBackStack: () -> Unit
){
    val uiState = settingViewModel.settingsUiState.collectAsState().value

    LanguageScreen(
        uiState = uiState,
        onEvent = settingViewModel::onEvent,
        onPopBackStack = onPopBackStack
    )
}