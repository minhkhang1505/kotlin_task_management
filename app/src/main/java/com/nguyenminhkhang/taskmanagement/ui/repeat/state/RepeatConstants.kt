package com.nguyenminhkhang.taskmanagement.ui.repeat.state

object RepeatConstants {
    val AvailableIntervals = listOf("Day", "Week", "Month", "Year")

    object MonthRepeatOptions {
        const val OnDate = "OnDate"
        const val OnDay = "OnDay"
        val all = listOf(OnDate, OnDay)
    }

    object DateItems {
        val all = listOf(
            "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
        )
    }

    object WeekDays {
        const val Monday = "Monday"
        const val Tuesday = "Tuesday"
        const val Wednesday = "Wednesday"
        const val Thursday = "Thursday"
        const val Friday = "Friday"
        const val Saturday = "Saturday"
        const val Sunday = "Sunday"

        val all = listOf(Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday)
    }

    object WeekOrder {
        const val First = "First"
        const val Second = "Second"
        const val Third = "Third"
        const val Fourth = "Fourth"
        const val Last = "Last"

        val all = listOf(First, Second, Third, Fourth, Last)
    }

    object EndCondition {
        const val Never = "Never"
        const val At = "At"
        const val After = "After"

        val all = listOf(Never, At, After)
    }
}