package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun TaskDetailPage(
    taskDetailViewModel: TaskDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by taskDetailViewModel.uiState.collectAsState()
    if (uiState.isLoading) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
        }
    } else {
        TaskDetailLayout(
            uiState = uiState,
            taskDetailViewModel = taskDetailViewModel,
            navController = navController)
    }
}