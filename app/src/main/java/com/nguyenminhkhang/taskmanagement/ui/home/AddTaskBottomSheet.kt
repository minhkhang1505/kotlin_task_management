package com.nguyenminhkhang.taskmanagement.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.common.components.CustomInputTextField
import com.nguyenminhkhang.taskmanagement.ui.common.components.RoundedOutlinedTextField
import com.nguyenminhkhang.taskmanagement.ui.home.event.HomeEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.TaskEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.UiEvent
import com.nguyenminhkhang.taskmanagement.ui.common.picker.DatePickerModal
import com.nguyenminhkhang.taskmanagement.ui.common.picker.TimePickerModal
import com.nguyenminhkhang.taskmanagement.ui.common.picker.convertMillisToDate
import com.nguyenminhkhang.taskmanagement.ui.home.state.HomeUiState
import com.nguyenminhkhang.taskmanagement.ui.common.pagertab.state.toHourMinute
import com.nguyenminhkhang.taskmanagement.ui.common.pagertab.state.toHourMinuteString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit
) {
    ModalBottomSheet(onDismissRequest = {
        onEvent(UiEvent.HideAddTaskSheet)
        onEvent(TaskEvent.NewTaskCleared)
    }) {
        CustomInputTextField(
            value= uiState.newTask?.content ?: "",
            onValueChange = { onEvent(TaskEvent.TaskContentChanged(it)) },
            placeholderDescription =  stringResource(R.string.new_task_name_description)
        )

        Spacer(modifier = Modifier.height(8.dp))
        if(uiState.isShowAddDetailTextField) {
            Text(stringResource(R.string.new_task_detail_title), modifier = Modifier.padding(horizontal = 16.dp))
            TextField(
                value=uiState.newTask?.taskDetail ?: "",
                onValueChange = { onEvent(TaskEvent.TaskDetailChanged(it)) },
                placeholder = { Text(stringResource(R.string.new_task_detail), style = TextStyle(fontSize = 12.sp, color = Color.Gray.copy(0.5f))) },
                modifier = Modifier
                    .padding(horizontal = 16.dp,)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(fontSize = 12.sp)
            )
        }
        if(uiState.newTask?.startDate != null || uiState.newTask?.startTime != null) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
            ) {
                val contentDateTime = StringBuilder()
                if (uiState.newTask.startDate != null) { contentDateTime.append(convertMillisToDate(uiState.newTask.startDate)) }
                if (uiState.newTask.startTime != null) {
                    contentDateTime.append(" on ")
                    contentDateTime.append(uiState.newTask.startTime.toHourMinuteString())
                }
                RoundedOutlinedTextField(
                    contentDateTime.toString(),
                    onClick = {
                        onEvent(TaskEvent.SelectedDateTimeCleared)
                    }
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                IconButton(
                    onClick = { onEvent(UiEvent.ShowAddDetailTextField) },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_more),
                        contentDescription = "Menu Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                IconButton(
                    onClick = { onEvent(UiEvent.ShowDatePicker) },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_calendar),
                        contentDescription = "Date Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                IconButton(
                    onClick = { onEvent(UiEvent.ShowTimePicker) }
                ) {
                    Icon(painter = painterResource(R.drawable.ic_clock),
                        contentDescription = "Time Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                IconButton(onClick = { onEvent(TaskEvent.ToggleNewTaskFavorite) }) {
                    Icon(painter = painterResource(R.drawable.ic_favorite),
                        contentDescription = "Favorite Icon",
                        tint = if (uiState.newTask?.favorite == true) Color.Yellow else MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }
            Button(
                onClick = {
                    onEvent(
                        TaskEvent.CombineDateAndTime(
                        date = uiState.newTask?.startDate ?: 0L,
                        hour = uiState.selectedReminderHour,
                        minute = uiState.selectedReminderMinute
                    ))
                    onEvent(TaskEvent.SaveNewTask)
                    onEvent(UiEvent.HideAddTaskSheet)
                    onEvent(TaskEvent.NewTaskCleared)
                }
            ) {
                Text(stringResource(R.string.save_button))
            }
        }

        if(uiState.isDatePickerVisible) {
            DatePickerModal(
                onDismiss = { onEvent(UiEvent.HideDatePicker) },
                onDateSelected = { onEvent(UiEvent.DateSelected(it)) },
            )
        }

        if (uiState.isTimePickerVisible) {
            TimePickerModal(
                onDismiss = { onEvent(UiEvent.HideTimePicker) },
                onConfirm = {time ->
                    val hour = time.hour
                    val minute = time.minute
                    onEvent(TaskEvent.OnReminderTimeSelected(hour, minute))
                    onEvent(UiEvent.TimeSelected(time.toHourMinute()))  },
            )
        }
    }
}