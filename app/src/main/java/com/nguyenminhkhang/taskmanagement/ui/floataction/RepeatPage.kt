package com.nguyenminhkhang.taskmanagement.ui.floataction

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
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.ui.datepicker.DatePickerModal
import com.nguyenminhkhang.taskmanagement.ui.datepicker.TimePickerModal
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toHourMinuteString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatPage(navController: NavController) {
    val dayItems = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    var timeAndDay by remember { mutableStateOf("") }
    var isSelectedDay by remember { mutableStateOf(false) }
    var isShowDatePicker by remember { mutableStateOf(false) }
    var selectedStartDay by remember { mutableStateOf("DD/MM/YYYY") }
    var selectedDays by remember { mutableStateOf("") }
    var isShowTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("Set time") }
    var repeatTimes by remember { mutableStateOf("1") }
    var isShowDropdownSelectedType by remember { mutableStateOf(false) }
    val repeatType = remember { mutableListOf("Day", "Week", "Month", "Year") }
    var selectedRepeatType by remember { mutableStateOf(repeatType[0]) }
    val radioOptions = listOf("Never", "At", "After")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

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
                            .clickable { /* Handle save action */ }
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Every")
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = "",
                    placeholder = { Text("1") },
                    onValueChange = { repeatTimes = it },
                    keyboardOptions = KeyboardOptions(keyboardType =  KeyboardType.Number),
                    modifier = Modifier.weight(0.2f).padding(end = 8.dp),
                )
                OutlinedTextField(
                    value = "",
                    placeholder = { Text(selectedRepeatType) },
                    onValueChange = {
                        isShowDropdownSelectedType = true
                        selectedRepeatType = it },
                    modifier = Modifier.padding(end = 8.dp).weight(0.8f),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                dayItems.forEach { day ->
                    DayItem(
                        day = day,
                        isSelected = false,
                        onClick = { isSelectedDay = !isSelectedDay }
                    )
                }
            }

            // Time Selection
            OutlinedTextField(
                value = selectedTime,
                onValueChange = {},
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth()
                    .clickable { isShowTimePicker = true },
                trailingIcon = {
                    Icon(Icons.Default.Clear, contentDescription = "Clear",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { selectedTime = "Set time" }
                    )
                },
                readOnly = true,
            )
            // Date Selection
            Text(text = "Start Date", modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
            OutlinedTextField(
                value = selectedStartDay,
                onValueChange = {},
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth()
                    .clickable { isShowDatePicker = true },
                readOnly = true,
            )
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
                                OutlinedTextField(
                                    value = selectedStartDay,
                                    onValueChange = { selectedStartDay = it },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                            "After" -> {
                                Text(
                                    text = "After",
                                    modifier = Modifier.padding(end = 8.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                OutlinedTextField(
                                    value = repeatTimes,
                                    onValueChange = { repeatTimes = it },
                                    modifier = Modifier
                                        .weight(0.2f)
                                        .padding(end = 8.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
            if(isShowDatePicker) {
                DatePickerModal(
                    onDismiss = { isShowDatePicker = false },
                    onDateSelected = { date ->
                        selectedStartDay = date
                        isShowDatePicker = false
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
        }
    }
}

@Composable
fun DayItem(day: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.border(
            width = 1.dp,
            shape = RoundedCornerShape(50.dp),
            color = MaterialTheme.colorScheme.primary,
        ).size(45.dp)
            .clickable { onClick() }
            .then(if (isSelected) Modifier.background(shape = RoundedCornerShape(50.dp), color = MaterialTheme.colorScheme.primary) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(text = day,
            modifier = Modifier
                .padding(8.dp),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}