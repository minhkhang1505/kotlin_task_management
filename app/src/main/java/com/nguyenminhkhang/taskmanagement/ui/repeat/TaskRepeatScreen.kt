package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.common.picker.DatePickerModal
import com.nguyenminhkhang.taskmanagement.ui.common.picker.TimePickerModal
import com.nguyenminhkhang.taskmanagement.ui.common.pagertab.state.toHourMinute
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskRepeatScreen(
    currentTask: RepeatUiState,
    onEvent: (RepeatEvent) -> Unit,
    onPopBackStack: () -> Unit,
    onScreenShown: () -> Unit
) {
    LaunchedEffect(Unit) {
        onScreenShown()
    }

    // Picker/dialog visibility — local screen state, not ViewModel concern
    var isDatePickerVisible by remember { mutableStateOf(false) }
    var isTimePickerVisible by remember { mutableStateOf(false) }
    var isEndDatePickerVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            RepeatTopAppBar(
                onSave = { onEvent(RepeatEvent.OnSaveRepeatTaskSetup) },
                onNavigationBack = { onPopBackStack() }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Repeat frequency input
            RepeatFrequencySelector(
                uiState = currentTask,
                onEvent = onEvent,
            )
            if (currentTask.draftTask!!.repeatInterval == "Week") {
                WeekDaySelector(
                    selectedDays = currentTask.draftTask.repeatDaysOfWeek ?: emptyList(),
                    onDayClick = { day -> onEvent(RepeatEvent.WeekDayClicked(day)) }
                )
            } else if (currentTask.draftTask.repeatInterval == "Month") {
                MonthlyRepeatOptions(
                    uiState = currentTask,
                    onEvent = onEvent
                )
            }
            // Time Selection
            RepeatTimeRow(
                uiState = currentTask,
                onShowTimePicker = { isTimePickerVisible = true },
                onClearDate = { onEvent(RepeatEvent.OnClearTimeSelected) }
            )
            // Start Date Selection
            RepeatStartDateRow(
                uiState = currentTask,
                onShowDatePicker = { isDatePickerVisible = true },
                onClearDate = { onEvent(RepeatEvent.OnClearStartDateSelected) }
            )
            // Radio Buttons for End Condition
            RepeatEndConditionSelector(
                uiState = currentTask,
                onEvent = onEvent,
                onShowEndDatePicker = { isEndDatePickerVisible = true }
            )
            if (isDatePickerVisible || isEndDatePickerVisible) {
                DatePickerModal(
                    onDismiss = {
                        isDatePickerVisible = false
                        isEndDatePickerVisible = false
                    },
                    onDateSelected = {
                        if (isEndDatePickerVisible) {
                            onEvent(RepeatEvent.OnEndDateSelected(it))
                        } else {
                            onEvent(RepeatEvent.OnStartDateSelected(it))
                        }
                        isDatePickerVisible = false
                        isEndDatePickerVisible = false
                    },
                )
            }
            if (isTimePickerVisible) {
                TimePickerModal(
                    onDismiss = { isTimePickerVisible = false },
                    onConfirm = {
                        onEvent(RepeatEvent.OnTimeSelected(it.toHourMinute()))
                        isTimePickerVisible = false
                    },
                )
            }
        }
    }
}