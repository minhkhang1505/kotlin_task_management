package com.nguyenminhkhang.taskmanagement.ui.settings.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.ui.NavScreen

@Composable
fun AccountPage(
    accountViewModel: AccountViewModel = hiltViewModel(),
    navController: NavController
) {
    val accountUiState by accountViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        accountViewModel.logoutEvent.collect {
            navController.navigate(NavScreen.LOGIN.route) {
                popUpTo(0)
            }
        }
    }

    AccountLayout(
        accountUiState = accountUiState,
        onEvent = accountViewModel::onEvent,
        navController = navController
    )
}