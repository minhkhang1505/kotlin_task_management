package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.RoundedOutlinedTextField
import com.nguyenminhkhang.taskmanagement.ui.datepicker.DatePickerModal
import com.nguyenminhkhang.taskmanagement.ui.datepicker.TimePickerModal
import com.nguyenminhkhang.taskmanagement.ui.datepicker.convertMillisToDate
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toHourMinuteString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailPage(taskDetailViewModel: TaskDetailViewModel = hiltViewModel(), navController: NavController) {
    val taskDetailDelegate = taskDetailViewModel as TaskDetailDelegate
    var detailInput by remember { mutableStateOf("") }
    var isShowDatePickerModel by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var isShowTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }
    val taskState by taskDetailViewModel.task.collectAsState()
    var repeatTime by remember { mutableStateOf("")}

    var titleChange by remember { mutableStateOf("") }
    titleChange = taskState.content

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        content = {
                            Icon(
                                painter = painterResource(
                                    id = if (isFavorite) {
                                        R.drawable.baseline_star_24
                                    } else {
                                        R.drawable.baseline_star_outline_24
                                    }
                                ),
                                contentDescription = "Favorite"
                            )
                        }
                    )
                },
                navigationIcon = {
                    Icon(Icons.Default.Clear, contentDescription = "Back",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                // Handle back navigation
                                navController.popBackStack()
                            }
                    )
                }
            )
        },

    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(8.dp).fillMaxSize()
        ) {
            Column() {
                DropdownMenu(
                    expanded = false,
                    onDismissRequest = { /* Handle dismiss */ },
                    modifier = Modifier.padding(8.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit Task") },
                        onClick = { /* Handle edit task */ }
                    )
                    DropdownMenuItem(
                        text = { Text("Edit Collection") },
                        onClick = { /* Handle edit task */ }
                    )
                }

                var isInEditMode by remember { mutableStateOf(false) }
                val focusRequester = remember { FocusRequester() }
                OutlinedTextField(
                    value = titleChange,
                    onValueChange = { titleChange = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused) {
                                isInEditMode = false
                            }
                        },
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    ),
                    trailingIcon = {
                        if (isInEditMode) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_done_all_24),
                                contentDescription = "Done Icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        isInEditMode = false
                                        taskDetailDelegate.updateTaskContentById(titleChange)
                                    }
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.baseline_edit_24),
                                contentDescription = "Edit Icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        isInEditMode = true
                                        focusRequester.requestFocus()
                                    }
                            )
                        }
                    },
                    enabled = isInEditMode
                )
                // menu icon sub task detail
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Menu, contentDescription = "Menu Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    OutlinedTextField(
                        value = detailInput,
                        onValueChange = { detailInput = it },
                        placeholder = { Text(text = "Add some detail") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp)
                            .border(
                                width = 0.dp,
                                color = Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent, // Màu border khi focus
                            unfocusedBorderColor = Color.Transparent // Màu border khi không focus

                        )
                    )
                }
                // date icon and text field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { isShowDatePickerModel = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.DateRange, contentDescription = "Date Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if(selectedDate.isEmpty()) {
                        Text(text = "Add date", modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        RoundedOutlinedTextField(
                            content = selectedDate,
                            onClick = { selectedDate = "" },
                        )
                    }
                }
                // time icon and text field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(50.dp)
                        .clickable { isShowTimePicker = true
                            Log.d("TaskDetailPage", "Time : ${repeatTime}") },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(painter = painterResource(R.drawable.baseline_access_time_24), contentDescription = "Time Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp)),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if(selectedTime.isEmpty()) {
                        Text(text = "Add time", modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        RoundedOutlinedTextField(
                            content = selectedTime,
                            onClick = { selectedTime = "" },
                        )
                    }
                }

                // repeat icon and text field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { navController.navigate("Repeat/${taskState.id}") },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(painter = painterResource(R.drawable.baseline_repeat_24), contentDescription = "Time Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp)),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if(repeatTime.isEmpty()) {
                        Text(text = "Set repeat times", modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        RoundedOutlinedTextField(
                            content = repeatTime,
                            onClick = { repeatTime = "" },
                        )
                    }
                }
            }

            if(isShowDatePickerModel) {
                DatePickerModal(
                    onDateSelected = { date ->
                        selectedDate = convertMillisToDate(date)
                    },
                    onDismiss = { isShowDatePickerModel = false }
                )
            }

            if (isShowTimePicker) {
                TimePickerModal(
                    onConfirm = { time ->
                        selectedTime = time.toHourMinuteString()
                    },
                    onDismiss = { isShowTimePicker = false }
                )
            }
            FloatingActionButton(
                onClick = {
                    taskState.id?.let { taskId ->
                        Log.d("DEBUG_FLOW", "1. GỬI KẾT QUẢ: Chuẩn bị gửi task ID = $taskId")
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
                Text(text = "Mark done", modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
