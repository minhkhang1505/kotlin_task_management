package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.ui.datepicker.DatePickerModal
import com.nguyenminhkhang.taskmanagement.ui.datepicker.TimePickerModal
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toHourMinute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailLayout(
    uiState: TaskDetailScreenUiState,
    taskDetailViewModel: TaskDetailViewModel = hiltViewModel(),
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TaskDetailTopAppBar(
                isFavorite = uiState.isFavorite,
                onFavoriteClick = {
                    taskDetailViewModel.onFavoriteChange()
                    taskDetailViewModel.onFavoriteClick()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        },
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Column {
                TaskTitleField(
                    title = uiState.task!!.content,
                    isInEditMode = uiState.isInEditMode,
                    onTitleChange = taskDetailViewModel::onTitleChange,
                    onEnterEditMode = taskDetailViewModel::onEnterEditMode,
                    onExitEditMode = taskDetailViewModel::onExitEditMode,
                    onSave = { taskDetailViewModel.saveTitle() }
                )
                // menu icon sub task detail
                TaskDetailInputRow(
                    detailValue = uiState.task.taskDetail,
                    onDetailChange = taskDetailViewModel::onDetailChange,
                    onSave = { taskDetailViewModel.saveDetail() }
                )
                // date icon and text field
                TaskDateRow(
                    uiState = uiState,
                    onShowDatePicker = taskDetailViewModel::onShowDatePicker,
                    onClearDate = taskDetailViewModel::onClearDateSelected
                )
                // time icon and text field
                TaskTimeRow(
                    uiState = uiState,
                    onShowDatePicker = taskDetailViewModel::onShowTimePicker,
                    onClearDate = taskDetailViewModel::onClearTimeSelected
                )
                // repeat icon and text field
                RepeatInfoRow(
                    summaryText = uiState.repeatSummaryText,
                    onClick = {
                        uiState.task?.id?.let { taskId ->
                            navController.navigate("Repeat/$taskId")
                        }
                    }
                )
            }

            if (uiState.isDatePickerVisible) {
                DatePickerModal(
                    onDateSelected = { taskDetailViewModel.onDateSelected(it) },
                    onDismiss = { taskDetailViewModel.onDismissDatePicker() },
                )
            }

            if (uiState.isTimePickerVisible) {
                TimePickerModal(
                    onConfirm = { taskDetailViewModel.onTimeSelected(it.toHourMinute()) },
                    onDismiss = { taskDetailViewModel.onDismissTimePicker() }
                )
            }

            FloatingActionButton(
                onClick = {
                    uiState.task?.id?.let { taskId ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("task_completed_id", taskId)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomEnd),
            ) {
                Text(
                    text = "Mark done", modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}