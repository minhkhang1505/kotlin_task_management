package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.NavigationEvent

@Composable
fun RepeatRoute(onPopBackStack: () -> Unit) {
    val repeatViewModel: RepeatViewModel = hiltViewModel()
    val currentTask by repeatViewModel.uiState.collectAsState()

    // Collect navigation events to navigate back after save
    LaunchedEffect(Unit) {
        repeatViewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateBackWithResult -> {
                    onPopBackStack()
                }
            }
        }
    }

    if (currentTask.isLoading) {
        CircularProgressIndicator()
    } else {
        TaskRepeatScreen(
            currentTask = currentTask,
            onPopBackStack = onPopBackStack,
            onEvent = repeatViewModel::onEvent,
            onScreenShown = repeatViewModel::onScreenShown
        )
    }
}
