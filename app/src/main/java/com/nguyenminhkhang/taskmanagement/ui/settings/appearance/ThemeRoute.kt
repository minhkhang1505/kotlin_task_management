package com.nguyenminhkhang.taskmanagement.ui.settings.appearance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.nguyenminhkhang.taskmanagement.ui.settings.account.SettingViewModel

@Composable
fun ThemeRoute(
    onPopBackStack: () -> Unit
) {
    val settingViewModel: SettingViewModel = hiltViewModel()
    val themeModeUiState by settingViewModel.themeUiState.collectAsState()

    ThemeScreen(
        themeModeUiState = themeModeUiState,
        onEvent = settingViewModel::onEvent,
        onPopBackStack = onPopBackStack
    )
}