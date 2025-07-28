package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun TaskDetailPage(
    taskDetailViewModel: TaskDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by taskDetailViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        taskDetailViewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateBackWithResult -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("task_completed_id", event.taskId)
                    navController.popBackStack()
                }
            }
        }
    }

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
            context = context,
            uiState = uiState,
            toggleFavorite = { taskDetailViewModel.toggleFavorite() },
            onTitleChange = { taskDetailViewModel.onTitleChange(it) },
            onEnterEditMode = { taskDetailViewModel.onEnterEditMode() },
            onExitEditMode = { taskDetailViewModel.onExitEditMode() },
            saveTitle = { taskDetailViewModel.saveTitle() },
            onDetailChange = { taskDetailViewModel.onDetailChange(it) },
            saveDetail = { taskDetailViewModel.saveDetail() },
            onShowDatePicker = { taskDetailViewModel.onShowDatePicker() },
            onClearDateSelected = { taskDetailViewModel.onClearDateSelected() },
            onShowTimePicker = { taskDetailViewModel.onShowTimePicker() },
            onClearTimeSelected = { taskDetailViewModel.onClearTimeSelected() },
            onDateSelected = { taskDetailViewModel.onDateSelected(it) },
            onDismissDatePicker = { taskDetailViewModel.onDismissDatePicker() },
            onTimeSelected = { taskDetailViewModel.onTimeSelected(it) },
            onDismissTimePicker = { taskDetailViewModel.onDismissTimePicker() },
            onNavigateBack = { navController.popBackStack() },
            onMarkAsDone = { taskDetailViewModel.onMarkAsDoneClicked() },
            onNavigateTo = { route -> navController.navigate(route) },
            onEvent = taskDetailViewModel::onEvent
        )
    }
}