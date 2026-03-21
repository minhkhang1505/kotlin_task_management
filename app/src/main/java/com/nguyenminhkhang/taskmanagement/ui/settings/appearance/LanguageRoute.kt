package com.nguyenminhkhang.taskmanagement.ui.settings.appearance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.nguyenminhkhang.taskmanagement.ui.settings.account.SettingViewModel

@Composable
fun LanguageRoute(
    onPopBackStack: () -> Unit
){
    val settingViewModel: SettingViewModel = hiltViewModel()
    val uiState = settingViewModel.uiState.collectAsState().value

    LanguageScreen(
        uiState = uiState,
        onEvent = settingViewModel::onEvent,
        onPopBackStack = onPopBackStack
    )
}