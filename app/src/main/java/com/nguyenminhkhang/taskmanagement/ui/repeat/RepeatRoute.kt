package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RepeatRoute(onPopBackStack: () -> Unit ) {
    val repeatViewModel: RepeatViewModel = hiltViewModel()
    val currentTask by repeatViewModel.uiState.collectAsState()
    if (currentTask.isLoading) {
        CircularProgressIndicator()
    } else {
        TaskRepeatScreen(
            currentTask = currentTask,
            onPopBackStack = onPopBackStack,
            onEvent = repeatViewModel::onEvent,
        )
    }
}

