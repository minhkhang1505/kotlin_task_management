package com.nguyenminhkhang.taskmanagement.ui.settings.appearance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.ui.settings.account.AccountViewModel

@Composable
fun LanguageRoute(
    onPopBackStack: () -> Unit
){
    val accountViewModel: AccountViewModel = hiltViewModel()
    val uiState = accountViewModel.uiState.collectAsState().value

    LanguageScreen(
        uiState = uiState,
        onEvent = accountViewModel::onEvent,
        onPopBackStack = onPopBackStack
    )
}