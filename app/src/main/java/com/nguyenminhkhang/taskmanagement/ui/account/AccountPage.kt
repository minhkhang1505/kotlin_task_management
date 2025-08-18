package com.nguyenminhkhang.taskmanagement.ui.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AccountPage(
    accountViewModel: AccountViewModel = hiltViewModel()
) {
    val accountUiState by accountViewModel.uiState.collectAsState()

    AccountLayout(
        accountUiState = accountUiState
    )
}