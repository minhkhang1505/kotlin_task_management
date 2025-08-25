package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun RepeatPage(navController: NavController, repeatViewModel: RepeatViewModel = hiltViewModel()) {
    val currentTask by repeatViewModel.uiState.collectAsState()
    if (currentTask.isLoading) {
        CircularProgressIndicator()
    } else {
        TaskRepeatLayout(
            currentTask = currentTask,
            onNavigationBack = { navController.popBackStack() },
            onEvent = repeatViewModel::onEvent,
        )
    }
}

