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
            onRepeatEveryChanged = repeatViewModel::onRepeatEveryChanged,
            onIntervalSelected = repeatViewModel::onIntervalSelected,
            onIntervalDropdownDismiss = repeatViewModel::onIntervalDropdownDismiss,
            onIntervalDropdownClicked = repeatViewModel::onIntervalDropdownClicked,
            onEvent = repeatViewModel::onEvent,
            onShowTimePicker = repeatViewModel::onShowTimePicker,
            onDismissTimePicker = repeatViewModel::onDismissTimePicker,
            onClearTimeSelected = repeatViewModel::onClearTimeSelected,
            onTimeSelected = repeatViewModel::onTimeSelected,
            onShowStartDatePicker = repeatViewModel::onShowDatePicker,
            onDismissStartDatePicker = repeatViewModel::onDismissDatePicker,
            onClearStartDateSelected = repeatViewModel::onClearDateSelected,
            onStartDateSelected = repeatViewModel::onDateSelected,
            onSave = repeatViewModel::updateTaskRepeatById,
            onNavigationBack = { navController.popBackStack() },
            onEndDateSelected = repeatViewModel::onEndDateSelected,
        )
    }
}

