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
    onEvent: (RepeatEvent) -> Unit,
    onNavigationBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            RepeatTopAppBar(
                onSave = { onEvent(RepeatEvent.OnSaveRepeatTaskSetup) },
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
                onEvent = onEvent,
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
                onShowTimePicker = { onEvent(RepeatEvent.OnShowTimePicker) },
                onClearDate = { onEvent(RepeatEvent.OnClearTimeSelected) }
            )
            // Start Date Selection
            RepeatStartDateRow(
                uiState = currentTask,
                onShowDatePicker = { onEvent(RepeatEvent.OnShowStartDatePicker) },
                onClearDate = { onEvent(RepeatEvent.OnClearStartDateSelected) }
            )
            // Radio Buttons for End Condition
            RepeatEndConditionSelector(
                uiState = currentTask,
                onEvent = onEvent,
            )
            if(currentTask.isDatePickerVisible || currentTask.isEndDatePickerVisible) {
                DatePickerModal(
                    onDismiss = {
                        onEvent(RepeatEvent.OnDismissStartDatePicker)
                        onEvent(RepeatEvent.DismissEndDatePicker) },
                    onDateSelected = {
                        if (currentTask.isEndDatePickerVisible) {
                            onEvent(RepeatEvent.OnEndDateSelected(it))
                        } else {
                            onEvent(RepeatEvent.OnStartDateSelected(it))
                        }
                    },
                )
            }
            if(currentTask.isTimePickerVisible) {
                TimePickerModal(
                    onDismiss = {
                        onEvent(RepeatEvent.OnDismissTimePicker) },
                    onConfirm = {
                        onEvent(RepeatEvent.OnTimeSelected(it.toHourMinute())) },
                )
            }
        }
    }
}