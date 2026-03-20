package com.nguyenminhkhang.taskmanagement.ui.settings.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun AccountRoute(
    onNavigateToLanguage: () -> Unit,
    onNavigateToTheme: () -> Unit
) {
    val accountViewModel: AccountViewModel = hiltViewModel()
    val accountUiState by accountViewModel.uiState.collectAsState()

//    LaunchedEffect(Unit) {
//        accountViewModel.logoutEvent.collect {
//            navController.navigate(NavScreen.SIGNIN.route) {
//                popUpTo(0)
//            }
//        }
//    }

    AccountScreen(
        accountUiState = accountUiState,
        onEvent = accountViewModel::onEvent,
        onNavigateToTheme = onNavigateToTheme,
        onNavigateToLanguage = onNavigateToLanguage
    )
}