package com.nguyenminhkhang.taskmanagement.ui.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.account.state.AccountUiState

@Composable
fun AccountLayout(
    accountUiState: AccountUiState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(it),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Account",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineLarge
                )
                Avatar(imageUrl = accountUiState.userAvatarUrl, initials = accountUiState.userName ?: "")
            }
        }
    }

}