package com.nguyenminhkhang.taskmanagement.ui.repeat.state

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState

data class RepeatUiState(
    val task: TaskUiState? = null,
    val isLoading: Boolean = true,
    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val availableIntervals: List<String> = listOf("Day", "Week", "Month", "Year"),

    val isIntervalDropdownVisible: Boolean = false,
    var textFieldWidth: Dp = 0.dp,
    val monthRepeatOptions: List<String> = listOf("OnDate", "OnDay"),
    val selectedMonthRepeatOption: String = "OnDate",

    val dayItems: List<String> = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
    val isSelectedDayOfWeek: Boolean = false,
    val selectedWeekDays: Set<String> = emptySet(),
    // State cho lựa chọn "OnDate"
    val selectedDayInMonth: Int = 1,

    // State cho lựa chọn "OnDay"
    val weekOrderOptions: List<String> = listOf("First", "Second", "Third", "Fourth", "Last"),
    val weekDayOptions: List<String> = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),

    val selectedWeekOrder: String = "First",
    val selectedWeekDay: String = "Monday",

    val endConditionOptions: List<String> = listOf("Never", "At", "After"),
    val selectedEndCondition: String = "Never",
    // State cho lựa chọn "At"
    //val endDate: Long? = null,
    val isEndDatePickerVisible: Boolean = false,

    // State cho lựa chọn "After"
    val occurrenceCount: String = "1"
)
