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
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.state.TaskDetailScreenUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailLayout(
    context: Context,
    uiState: TaskDetailScreenUiState,
    onNavigateBack: () -> Unit,
    onNavigateTo: (String) -> Unit,
    onEvent: (TaskDetailEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TaskDetailTopAppBar(
                isFavorite = uiState.task?.favorite ?: false,
                onFavoriteClick = { onEvent(TaskDetailEvent.ToggleFavorite) },
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
                // collection name row
                CurrentCollectionRow(
                    collectionName = uiState.currentCollection,
                    onCollectionClick = { onEvent(TaskDetailEvent.ShowChangeCollectionSheet) }
                )
                // task title and edit icon
                TaskTitleField(
                    title = uiState.task!!.content,
                    isInEditMode = uiState.isInEditMode,
                    onTitleChange = { onEvent(TaskDetailEvent.OnTitleChanged(it)) },
                    onEnterEditMode = { onEvent(TaskDetailEvent.OnEnterEditMode) },
                    onExitEditMode = { onEvent(TaskDetailEvent.OnExitEditMode) },
                    onSave = { onEvent(TaskDetailEvent.SaveTitle) }
                )
                // menu icon sub task detail
                TaskDetailInputRow(
                    detailValue = uiState.task.taskDetail,
                    onDetailChange = { onEvent(TaskDetailEvent.OnDetailChange(it)) },
                    onSave = { onEvent(TaskDetailEvent.SaveDetail) }
                )
                // date icon and text field
                TaskDateRow(
                    uiState = uiState,
                    onShowDatePicker = { onEvent(TaskDetailEvent.OnShowDatePicker) },
                    onClearDate = { onEvent(TaskDetailEvent.OnClearDateSelected) }
                )
                // time icon and text field
                TaskTimeRow(
                    uiState = uiState,
                    onShowDatePicker = { onEvent(TaskDetailEvent.OnShowTimePicker) },
                    onClearDate = { onEvent(TaskDetailEvent.OnClearTimeSelected) }
                )
                // repeat icon and text field
                RepeatInfoRow(
                    summaryText = uiState.repeatSummaryText,
                    onClick = {
                        onNavigateTo("Repeat/${uiState.task.id}")
                    }
                )
                AddToCalendarButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp),
                    onClick = { onEvent(TaskDetailEvent.AddToCalendar(context, uiState.task)) }
                )
            }

            if (uiState.isDatePickerVisible) {
                DatePickerModal(
                    onDateSelected = { onEvent(TaskDetailEvent.OnDateSelected(it)) },
                    onDismiss = { onEvent(TaskDetailEvent.OnDismissDatePicker) },
                )
            }

            if (uiState.isTimePickerVisible) {
                TimePickerModal(
                    onConfirm = { onEvent(TaskDetailEvent.OnTimeSelected(it.toHourMinute())) },
                    onDismiss = { onEvent(TaskDetailEvent.OnDismissTimePicker) }
                )
            }
            if (uiState.isChangeCollectionSheetVisible) {
                ModelButtonChangeCollection(
                    uiState = uiState,
                    onEvent = onEvent
                )
            }

            FloatingActionButton(
                onClick = { onEvent(TaskDetailEvent.OnMarkAsDoneClicked) },
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