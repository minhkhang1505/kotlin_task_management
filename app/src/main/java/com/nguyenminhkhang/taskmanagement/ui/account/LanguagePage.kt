package com.nguyenminhkhang.taskmanagement.ui.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun LanguagePage(
    accountViewModel: AccountViewModel = hiltViewModel(),
    navController: NavController
){
    val uiState = accountViewModel.uiState.collectAsState().value

    LanguageLayout(
        uiState = uiState,
        onEvent = accountViewModel::onEvent,
        navController = navController
    )
}