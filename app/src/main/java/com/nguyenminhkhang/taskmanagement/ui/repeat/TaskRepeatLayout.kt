package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.datepicker.DatePickerModal
import com.nguyenminhkhang.taskmanagement.ui.datepicker.TimePickerModal
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toHourMinute
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskRepeatLayout(
    currentTask: RepeatUiState,
    onRepeatEveryChanged: (Long) -> Unit,
    onIntervalSelected: (String) -> Unit,
    onIntervalDropdownDismiss: () -> Unit,
    onIntervalDropdownClicked: () -> Unit,
    onEvent: (RepeatEvent) -> Unit,
    onShowTimePicker: () -> Unit,
    onDismissTimePicker: () -> Unit,
    onClearTimeSelected: () -> Unit,
    onTimeSelected: (Long) -> Unit,
    onShowStartDatePicker: () -> Unit,
    onDismissStartDatePicker: () -> Unit,
    onClearStartDateSelected: () -> Unit,
    onStartDateSelected: (Long) -> Unit,
    onSave: () -> Unit,
    onNavigationBack: () -> Unit,
    onEndDateSelected: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            RepeatTopAppBar(
                onSave = { onSave() },
                onNavigationBack = { onNavigationBack() }
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
                onRepeatEveryChanged = onRepeatEveryChanged,
                onIntervalSelected = onIntervalSelected,
                onIntervalDropdownDismiss = onIntervalDropdownDismiss,
                onIntervalDropdownClicked = onIntervalDropdownClicked,
            )
            if (currentTask.task!!.repeatInterval == "Week") {
                WeekDaySelector(
                    allDays = currentTask.dayItems,
                    selectedDays = currentTask.task.repeatDaysOfWeek ?: emptyList(),
                    onDayClick = { day -> onEvent(RepeatEvent.WeekDayClicked(day))  }
                )
            } else if (currentTask.task.repeatInterval == "Month") {
                MonthlyRepeatOptions(
                    uiState = currentTask,
                    onEvent = onEvent
                )
            }
            // Time Selection
            RepeatTimeRow(
                uiState = currentTask,
                onShowTimePicker = { onShowTimePicker() },
                onClearDate = { onClearTimeSelected() }
            )
            // Start Date Selection
            RepeatStartDateRow(
                uiState = currentTask,
                onShowDatePicker = { onShowStartDatePicker() },
                onClearDate = { onClearStartDateSelected() }
            )
            // Radio Buttons for End Condition
            RepeatEndConditionSelector(
                uiState = currentTask,
                onEvent = onEvent,
            )
            if(currentTask.isDatePickerVisible || currentTask.isEndDatePickerVisible) {
                DatePickerModal(
                    onDismiss = { onDismissStartDatePicker()
                                onEvent(RepeatEvent.DismissEndDatePicker) },
                    onDateSelected = {
                        if (currentTask.isEndDatePickerVisible) {
                            onEndDateSelected(it)
                        } else {
                            onStartDateSelected(it)
                        }
                    },
                )
            }
            if(currentTask.isTimePickerVisible) {
                TimePickerModal(
                    onDismiss = { onDismissTimePicker() },
                    onConfirm = { onTimeSelected(it.toHourMinute()) },
                )
            }
        }
    }
}