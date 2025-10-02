package com.nguyenminhkhang.taskmanagement.ui.repeat.state

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity

data class RepeatUiState(
    val task: TaskEntity? = null,
    val isLoading: Boolean = true,

    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,

    val isIntervalDropdownVisible: Boolean = false,
    var textFieldWidth: Dp = 0.dp,

    val selectedMonthRepeatOption: String = RepeatConstants.MonthRepeatOptions.OnDate,
    val isSelectedDayOfWeek: Boolean = false,
    val selectedWeekDays: Set<String> = emptySet(),

    // State cho lựa chọn "OnDate"
    val selectedDayInMonth: Int = 1,

    val selectedWeekOrder: String = RepeatConstants.WeekOrder.First,
    val selectedWeekDay: String = RepeatConstants.WeekDays.Monday,

    val selectedEndCondition: String = RepeatConstants.EndCondition.Never,
    val isEndDatePickerVisible: Boolean = false,

    // State cho lựa chọn "After"
    val occurrenceCount: String = "1"
)