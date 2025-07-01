package com.nguyenminhkhang.taskmanagement.ui.repeat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.ui.datepicker.DatePickerModal
import com.nguyenminhkhang.taskmanagement.ui.datepicker.TimePickerModal
import com.nguyenminhkhang.taskmanagement.ui.datepicker.convertMillisToDate
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toHourMinute
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toHourMinuteString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatPage(navController: NavController, repeatViewModel: RepeatViewModel = hiltViewModel(), taskId: Long?) {
    val currentTask by repeatViewModel.task.collectAsState()
    if (currentTask.id == null) {
        CircularProgressIndicator()
    } else {
        val repeatDelegate = repeatViewModel as RepeatDelegate
        val dayItems = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val setRepeatDaysOfWeek = remember { mutableStateOf(currentTask.repeatDaysOfWeek) }
        var isShowStartDatePicker by remember { mutableStateOf(false) }
        var isShowEndDatePicker by remember { mutableStateOf(false) }
        var selectedRepeatStartDay by remember { mutableStateOf(currentTask.startDate) }
        var selectedRepeatEndDate by remember { mutableStateOf(currentTask.repeatEndDate) }
        var isShowTimePicker by remember { mutableStateOf(false) }
        var selectedTime by remember { mutableStateOf(currentTask.startTime?.toHourMinuteString()) }
        var setRepeatEvery by remember { mutableStateOf(currentTask.repeatEvery.toString()) }
        var selectedRepeatEndCount by remember { mutableStateOf(currentTask.repeatEndCount.toString()) }
        var isShowDropdownSelectedType by remember { mutableStateOf(false) }
        val repeatIntervalList = remember { mutableListOf("Day", "Week", "Month", "Year") }
        var selectedRepeatInterval by remember { mutableStateOf(currentTask.repeatInterval) }
        val radioOptions = listOf("Never", "At", "After")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(currentTask.repeatEndType) }

        val OUTLINETEXTFIELD_COLOR = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = Color.Transparent,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Repeat") },
                    navigationIcon = {
                        Icon(Icons.Default.Clear, contentDescription = "Back",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { navController.popBackStack() }
                        )
                    },
                    actions = {
                        Text("Save",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    Log.d("RepeatPage", "Saving repeat settings for task ID: $taskId")
                                    Log.d("RepeatPage", "Repeat Every: $setRepeatEvery")
                                    Log.d("RepeatPage", "Repeat Days of Week: ${setRepeatDaysOfWeek.value?.joinToString(",")}")
                                    Log.d("RepeatPage", "Repeat Interval: $selectedRepeatInterval")
                                    Log.d("RepeatPage", "Repeat End Type: $selectedOption")
                                    Log.d("RepeatPage", "Repeat End Date: $selectedRepeatEndDate")
                                    Log.d("RepeatPage", "Repeat End Count: $selectedRepeatEndCount")
                                    repeatDelegate.updateTaskRepeatById(
                                        taskId = taskId!!,
                                        repeatEvery = setRepeatEvery.toLongOrNull() ?: 1L,
                                        repeatDaysOfWeek = setRepeatDaysOfWeek.value?.joinToString(","),
                                        repeatInterval = selectedRepeatInterval,
                                        repeatEndType = selectedOption,
                                        repeatEndDate = selectedRepeatEndDate,
                                        repeatEndCount = selectedRepeatEndCount.toInt(),
                                        startTime = selectedTime?.toHourMinute()
                                    )
                                    navController.popBackStack()
                                }
                        )
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Every")
                Row (
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val focusManager = LocalFocusManager.current
                    OutlinedTextField(
                        value = setRepeatEvery.toString(),
                        placeholder = { Text("1") },
                        onValueChange = { setRepeatEvery = it },
                        keyboardOptions = KeyboardOptions(keyboardType =  KeyboardType.Number, imeAction = ImeAction.Done),
                        modifier = Modifier
                            .weight(0.2f)
                            .padding(end = 8.dp),
                        maxLines = 1,
                        singleLine = true,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        )
                    )
                    var textFieldWidth by remember { mutableStateOf(0.dp) }
                    val density = LocalDensity.current
                    Box (
                        modifier = Modifier
                            .onSizeChanged { size ->
                                textFieldWidth = with(density) { size.width.toDp() }
                            }
                            .clickable { isShowDropdownSelectedType = !isShowDropdownSelectedType }
                    ) {
                        OutlinedTextField(
                            value = selectedRepeatInterval ?: "Select Interval",
                            onValueChange = {
                                isShowDropdownSelectedType = true
                                selectedRepeatInterval = it },
                            trailingIcon = {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Dropdown",
                                    modifier = Modifier.padding(8.dp)
                                )
                            },
                            readOnly = true,
                            enabled = false,
                            colors = OUTLINETEXTFIELD_COLOR,
                        )
                        DropdownMenu(
                            expanded = isShowDropdownSelectedType,
                            onDismissRequest = { isShowDropdownSelectedType = false },
                            modifier = Modifier.width(textFieldWidth)
                        ) {
                            repeatIntervalList.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        selectedRepeatInterval = type
                                        isShowDropdownSelectedType = false
                                    },
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    dayItems.forEach { day ->
                        DayChip(
                            day = day,
                            isSelected = setRepeatDaysOfWeek.value?.contains(day) ?: false,
                            onDayClick = {clickedDay->
                                val currentSelection = setRepeatDaysOfWeek.value!!.toMutableSet()
                                if( currentSelection.contains(clickedDay)) {
                                    currentSelection.remove(clickedDay)
                                } else {
                                    currentSelection.add(clickedDay)
                                }
                                setRepeatDaysOfWeek.value = currentSelection
                            }
                        )
                    }
                }

                // Time Selection
                Box(modifier = Modifier.clickable { isShowTimePicker = true }) {
                    OutlinedTextField(
                        value = if (selectedTime != null) selectedTime!! else "Select Time",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth(),
                        trailingIcon = {
                            Icon(Icons.Default.Clear, contentDescription = "Clear",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable { selectedTime = null }
                            )
                        },
                        readOnly = true,
                        enabled = false,
                        colors = OUTLINETEXTFIELD_COLOR
                    )
                }

                // Start Date Selection
                Text(text = "Start Date", modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
                Box(modifier = Modifier.clickable { isShowStartDatePicker = true }) {
                    OutlinedTextField(
                        value = if(selectedRepeatStartDay != null) convertMillisToDate(selectedRepeatStartDay!!) else "",
                        placeholder = { Text("Select Start Date") },
                        onValueChange = {selectedRepeatStartDay = it.toLongOrNull()},
                        modifier = Modifier
                            .fillMaxWidth(),
                        readOnly = true,
                        enabled = false,
                        colors = OUTLINETEXTFIELD_COLOR
                    )
                }
                // Radio Buttons for End Condition
                Text(text = "End Condition", modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
                Column(
                    modifier = Modifier.selectableGroup()
                ) {
                    radioOptions.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (option == selectedOption),
                                onClick = { onOptionSelected(option) },
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            when (option) {
                                "Never" -> {
                                    Text(
                                        text = option,
                                        modifier = Modifier.padding(end = 8.dp),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                "At" -> {
                                    Text(
                                        text = "On",
                                        modifier = Modifier.padding(end = 8.dp),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Box(modifier = Modifier
                                        .selectable(
                                            selected = ("At" == selectedOption),
                                            onClick = {
                                                onOptionSelected("At")
                                                isShowEndDatePicker = true
                                            },
                                            role = Role.RadioButton
                                        )
                                    ) {
                                        OutlinedTextField(
                                            value = if(selectedRepeatEndDate != null) convertMillisToDate(selectedRepeatEndDate!!) else "",
                                            placeholder = { Text("Select End Date") },
                                            onValueChange = {},
                                            readOnly = true,
                                            enabled = false,
                                            colors = OUTLINETEXTFIELD_COLOR
                                        )
                                    }
                                }
                                "After" -> {
                                    Text(
                                        text = "After",
                                        modifier = Modifier.padding(end = 8.dp),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    val focusManager = LocalFocusManager.current
                                    OutlinedTextField(
                                        value = selectedRepeatEndCount,
                                        placeholder = { Text("1") },
                                        onValueChange = { selectedRepeatEndCount = it },
                                        modifier = Modifier
                                            .weight(0.2f)
                                            .padding(end = 8.dp)
                                            .selectable(
                                                selected = ("After" == selectedOption),
                                                onClick = { onOptionSelected("After") },
                                                role = Role.RadioButton
                                            ),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                        enabled = ("After" == selectedOption),
                                        colors = OUTLINETEXTFIELD_COLOR,
                                        maxLines = 1,
                                        singleLine = true,
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                focusManager.clearFocus()
                                            }
                                        )
                                    )
                                    Text(
                                        text = "occurrence",
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .weight(0.6f),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
                if(isShowStartDatePicker || isShowEndDatePicker) {
                    DatePickerModal(
                        onDismiss = { isShowStartDatePicker = false
                            isShowEndDatePicker = false },
                        onDateSelected = { date ->
                            if(isShowStartDatePicker) selectedRepeatStartDay = date else selectedRepeatEndDate = date
                            isShowStartDatePicker = false
                            isShowEndDatePicker = false
                        },
                    )
                }
                if(isShowTimePicker) {
                    TimePickerModal(
                        onDismiss = { isShowTimePicker = false },
                        onConfirm = { time ->
                            selectedTime = time.toHourMinuteString()
                            isShowTimePicker = false
                        },
                    )
                }
                if(isShowDropdownSelectedType){}
            }
        }
    }
}

@Composable
fun DayChip(
    day: String,
    isSelected: Boolean,
    onDayClick: (String) -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(45.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
            // Xử lý sự kiện click
            .clickable { onDayClick(day) }
    ) {
        Text(
            text = day,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}