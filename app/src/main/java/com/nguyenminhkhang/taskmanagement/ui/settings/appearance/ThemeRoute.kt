package com.nguyenminhkhang.taskmanagement.ui.settings.appearance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nguyenminhkhang.taskmanagement.ui.settings.account.SettingViewModel

@Composable
fun ThemeRoute(
    settingViewModel: SettingViewModel,
    onPopBackStack: () -> Unit
) {
    val themeModeUiState by settingViewModel.themeUiState.collectAsState()

    ThemeScreen(
        themeModeUiState = themeModeUiState,
        onEvent = settingViewModel::onEvent,
        onPopBackStack = onPopBackStack
    )
}