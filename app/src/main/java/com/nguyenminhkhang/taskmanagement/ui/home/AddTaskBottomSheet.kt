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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.common.RoundedOutlinedTextField
import com.nguyenminhkhang.taskmanagement.ui.datepicker.DatePickerModal
import com.nguyenminhkhang.taskmanagement.ui.datepicker.TimePickerModal
import com.nguyenminhkhang.taskmanagement.ui.datepicker.convertMillisToDate
import com.nguyenminhkhang.taskmanagement.ui.home.state.NewTaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toHourMinute
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toHourMinuteString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    uiState: NewTaskUiState,
    onEvent: (HomeEvent) -> Unit
) {
    ModalBottomSheet(onDismissRequest = { onEvent(HomeEvent.HideAddTaskSheet) }) {
        TextField(
            value= uiState.newTaskContent,
            onValueChange = { onEvent(HomeEvent.TaskContentChanged(it)) },
            placeholder = { Text("What’s your next task?", style = TextStyle(color = Color.Gray.copy(0.5f))) },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        if(uiState.isShowAddDetailTextField) {
            Text("Detail", modifier = Modifier.padding(horizontal = 16.dp))
            TextField(
                value=uiState.newTaskDetail,
                onValueChange = { onEvent(HomeEvent.TaskDetailChanged(it)) },
                placeholder = { Text("Add detail", style = TextStyle(fontSize = 12.sp, color = Color.Gray.copy(0.5f))) },
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
        if(uiState.selectedDate != null || uiState.selectedTime != null) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
            ) {
                val contentDateTime = StringBuilder()
                if (uiState.selectedDate != null) { contentDateTime.append(convertMillisToDate(uiState.selectedDate)) }
                if (uiState.selectedTime != null) {
                    contentDateTime.append(" on ")
                    contentDateTime.append(uiState.selectedTime.toHourMinuteString())
                }
                RoundedOutlinedTextField(
                    contentDateTime.toString(),
                    onClick = {
                        onEvent(HomeEvent.ClearSelectedDateTime(uiState.selectedDate, uiState.selectedTime))
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
                    onClick = { onEvent(HomeEvent.ShowAddDetailTextField) },
                ) {
                    Icon(
                        Icons.Default.Menu, contentDescription = "Menu Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                IconButton(
                    onClick = { onEvent(HomeEvent.ShowDatePicker) },
                ) {
                    Icon(
                        Icons.Default.DateRange, contentDescription = "Date Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                IconButton(
                    onClick = { onEvent(HomeEvent.ShowTimePicker) }
                ) {
                    Icon(painter = painterResource(R.drawable.baseline_access_time_24), contentDescription = "Time Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                IconButton(onClick = { onEvent(HomeEvent.ToggleNewTaskFavorite) }) {
                    Icon(painter = if(uiState.newTaskIsFavorite) {
                        painterResource(R.drawable.baseline_star_24)
                    } else {
                        painterResource(R.drawable.baseline_star_outline_24)
                    }, contentDescription = "Favorite Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }
            Button(
                onClick = {
                    onEvent(HomeEvent.SaveNewTask)
                    onEvent(HomeEvent.HideAddTaskSheet)
                    onEvent(HomeEvent.ClearNewTask)
                }
            ) {
                Text("Save")
            }
        }

        if(uiState.isDatePickerVisible) {
            DatePickerModal(
                onDismiss = { onEvent(HomeEvent.HideDatePicker) },
                onDateSelected = { onEvent(HomeEvent.DateSelected(it)) },
            )
        }

        if (uiState.isTimePickerVisible) {
            TimePickerModal(
                onDismiss = { onEvent(HomeEvent.HideTimePicker) },
                onConfirm = { onEvent(HomeEvent.TimeSelected(it.toHourMinute()))  },
            )
        }
    }
}