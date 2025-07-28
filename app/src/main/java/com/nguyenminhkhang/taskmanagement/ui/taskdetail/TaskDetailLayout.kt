package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import android.content.Context
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
import com.nguyenminhkhang.taskmanagement.ui.datepicker.DatePickerModal
import com.nguyenminhkhang.taskmanagement.ui.datepicker.TimePickerModal
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toHourMinute
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTaskEntity
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.state.TaskDetailScreenUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailLayout(
    context: Context,
    uiState: TaskDetailScreenUiState,
    toggleFavorite: () -> Unit,
    onTitleChange: (String) -> Unit,
    onEnterEditMode: () -> Unit,
    onExitEditMode: () -> Unit,
    saveTitle: () -> Unit,
    onDetailChange: (String) -> Unit,
    saveDetail: () -> Unit,
    onShowDatePicker: () -> Unit,
    onClearDateSelected: () -> Unit,
    onShowTimePicker: () -> Unit,
    onClearTimeSelected: () -> Unit,
    onDateSelected: (Long) -> Unit,
    onDismissDatePicker: () -> Unit,
    onTimeSelected: (Long) -> Unit,
    onDismissTimePicker: () -> Unit,
    onNavigateBack: () -> Unit,
    onMarkAsDone: () -> Unit,
    onNavigateTo: (String) -> Unit,
    onEvent: (TaskDetailEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TaskDetailTopAppBar(
                isFavorite = uiState.task?.isFavorite ?: false,
                onFavoriteClick = { toggleFavorite() },
                onNavigateBack = { onNavigateBack() },
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
                // task title and edit icon
                TaskTitleField(
                    title = uiState.task!!.content,
                    isInEditMode = uiState.isInEditMode,
                    onTitleChange = onTitleChange,
                    onEnterEditMode = onEnterEditMode,
                    onExitEditMode = onExitEditMode,
                    onSave = { saveTitle() }
                )
                // menu icon sub task detail
                TaskDetailInputRow(
                    detailValue = uiState.task.taskDetail,
                    onDetailChange = onDetailChange,
                    onSave = { saveDetail() }
                )
                // date icon and text field
                TaskDateRow(
                    uiState = uiState,
                    onShowDatePicker = onShowDatePicker,
                    onClearDate = onClearDateSelected
                )
                // time icon and text field
                TaskTimeRow(
                    uiState = uiState,
                    onShowDatePicker = onShowTimePicker,
                    onClearDate = onClearTimeSelected
                )
                // repeat icon and text field
                RepeatInfoRow(
                    summaryText = uiState.repeatSummaryText,
                    onClick = {
                        onNavigateTo("Repeat/${uiState.task.id}")
                    }
                )
                AddToCalendarButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp),
                    onClick = { onEvent(TaskDetailEvent.AddToCalendar(context, uiState.task.toTaskEntity())) }
                )
            }

            if (uiState.isDatePickerVisible) {
                DatePickerModal(
                    onDateSelected = { onDateSelected(it) },
                    onDismiss = { onDismissDatePicker() },
                )
            }

            if (uiState.isTimePickerVisible) {
                TimePickerModal(
                    onConfirm = { onTimeSelected(it.toHourMinute()) },
                    onDismiss = { onDismissTimePicker() }
                )
            }

            FloatingActionButton(
                onClick = onMarkAsDone,
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