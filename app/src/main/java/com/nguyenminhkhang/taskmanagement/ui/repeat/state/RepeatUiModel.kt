package com.nguyenminhkhang.taskmanagement.ui.repeat.state

data class RepeatUiModel(
    val id: Int? = null,
    val repeatInterval: String = "Day", // Day, Week, Month, Year
    val repeatDaysOfWeek: List<String>? = null, // Only for weekly repeat
    val repeatMonthlyOption: String = "OnDate", // OnDate, OnDay
    val repeatMonthlyDate: Int? = null, // Only for monthly repeat with OnDate option
    val repeatMonthlyWeekOrder: String? = null, // Only for monthly repeat with OnDay option
    val repeatMonthlyDayOfWeek: String? = null, // Only for monthly repeat with OnDay option
    val startDate: Long? = null,
    val timeOfDay: Long? = null,
    val endCondition: String = "Never", // Never, At, After
    val endDate: Long? = null, // Only for end condition At
)


fun RepeatUiModel.toRepeatEntity(): RepeatEntity {
    return RepeatEntity(
        id = this.id,
        repeatInterval = this.repeatInterval,
        repeatDaysOfWeek = this.repeatDaysOfWeek,
        repeatMonthlyOption = this.repeatMonthlyOption,
        repeatMonthlyDate = this.repeatMonthlyDate,
        repeatMonthlyWeekOrder = this.repeatMonthlyWeekOrder,
        repeatMonthlyDayOfWeek = this.repeatMonthlyDayOfWeek,
        startDate = this.startDate,
        timeOfDay = this.timeOfDay,
        endCondition = this.endCondition,
        endDate = this.endDate,
    )
}