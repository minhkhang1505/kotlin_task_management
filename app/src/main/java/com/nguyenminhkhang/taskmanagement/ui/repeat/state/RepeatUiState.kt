package com.nguyenminhkhang.taskmanagement.ui.repeat.state

import com.nguyenminhkhang.shared.model.Task


data class RepeatUiState(
    val originalTask: Task? = null,
    val draftTask: Task? = null,
    val isLoading: Boolean = true,

    val selectedMonthRepeatOption: String = RepeatConstants.MonthRepeatOptions.OnDate,
    val isSelectedDayOfWeek: Boolean = false,
    val selectedWeekDays: Set<String> = emptySet(),

    // State cho lựa chọn "OnDate"
    val selectedDayInMonth: Int = 1,

    val selectedWeekOrder: String = RepeatConstants.WeekOrder.First,
    val selectedWeekDay: String = RepeatConstants.WeekDays.Monday,

    val selectedEndCondition: String = RepeatConstants.EndCondition.Never,

    // State cho lựa chọn "After"
    val occurrenceCount: String = "1"
)