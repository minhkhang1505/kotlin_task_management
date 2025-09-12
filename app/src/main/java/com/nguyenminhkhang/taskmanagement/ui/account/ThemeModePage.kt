package com.nguyenminhkhang.taskmanagement.ui.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ThemeModePage(
    accountViewModel: AccountViewModel = hiltViewModel(),
    navController: NavController
) {
    val themeModeUiState by accountViewModel.themeUiState.collectAsState()

    ThemeModeLayout(
        themeModeUiState = themeModeUiState,
        onEvent = accountViewModel::onEvent,
        navController = navController
    )
}