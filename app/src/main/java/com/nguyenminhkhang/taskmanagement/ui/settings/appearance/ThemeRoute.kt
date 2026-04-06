package com.nguyenminhkhang.taskmanagement.ui.settings.appearance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.SettingViewModel

@Composable
fun ThemeRoute(
    settingViewModel: SettingViewModel,
    onPopBackStack: () -> Unit
) {
    val themeModeUiState by settingViewModel.themeUiState.collectAsState()
    val settingUiState by settingViewModel.settingsUiState.collectAsState()

    ThemeScreen(
        themeModeUiState = themeModeUiState,
        settingUiState = settingUiState,
        onEvent = settingViewModel::onEvent,
        onPopBackStack = onPopBackStack
    )
}