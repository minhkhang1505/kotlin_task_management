package com.nguyenminhkhang.taskmanagement.ui.repeat.state

import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity

data class RepeatUiState(
    val originalTask: TaskEntity? = null,
    val draftTask: TaskEntity? = null,
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