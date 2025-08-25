package com.nguyenminhkhang.taskmanagement.ui.repeat

sealed class RepeatEvent {
    data class MonthRepeatOptionChanged(val option: String) : RepeatEvent()
    data class DayInMonthChanged(val day: Int) : RepeatEvent()
    data class WeekOrderChanged(val order: String) : RepeatEvent()
    data class WeekDayChanged(val day: String) : RepeatEvent()

    data class WeekDayClicked(val day: String) : RepeatEvent()

    data class EndConditionChanged(val option: String) : RepeatEvent()
    object DismissEndDatePicker : RepeatEvent()
    data class OccurrenceCountChanged(val count: String) : RepeatEvent()

    data class OnRepeatEveryChanged(val repeatEvery: Long) : RepeatEvent()
    data class OnIntervalSelected(val intervalSelected: String) : RepeatEvent()
    object OnIntervalDropdownDismiss : RepeatEvent()
    object OnIntervalDropdownClicked : RepeatEvent()

    object OnShowTimePicker : RepeatEvent()
    object OnDismissTimePicker : RepeatEvent()
    object OnClearTimeSelected : RepeatEvent()
    data class OnTimeSelected(val timeMillis: Long) : RepeatEvent()

    object OnShowStartDatePicker : RepeatEvent()
    object OnDismissStartDatePicker : RepeatEvent()
    object OnClearStartDateSelected : RepeatEvent()
    data class OnStartDateSelected(val dateMillis: Long) : RepeatEvent()

    object OnSaveRepeatTaskSetup : RepeatEvent()

    object ShowEndDatePicker : RepeatEvent()
    data class OnEndDateSelected(val dateMillis: Long) : RepeatEvent()
}