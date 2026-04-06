package com.nguyenminhkhang.taskmanagement.ui.settings.appearance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.SettingViewModel

@Composable
fun FontStyleRoute(
    settingViewModel: SettingViewModel,
    onPopBackStack: () -> Unit
){
    val uiState = settingViewModel.settingsUiState.collectAsState().value

    FontStyleScreen(
        uiState = uiState,
        onEvent = settingViewModel::onEvent,
        onPopBackStack = onPopBackStack
    )
}
