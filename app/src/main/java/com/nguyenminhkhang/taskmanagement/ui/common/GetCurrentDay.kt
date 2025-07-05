package com.nguyenminhkhang.taskmanagement.ui.common

fun getCurrentDay(): String {
    val calendar = java.util.Calendar.getInstance()
    val dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK)
    return when (dayOfWeek) {
        java.util.Calendar.SUNDAY -> "Sun"
        java.util.Calendar.MONDAY -> "Mon"
        java.util.Calendar.TUESDAY -> "Tue"
        java.util.Calendar.WEDNESDAY -> "Wed"
        java.util.Calendar.THURSDAY -> "Thu"
        java.util.Calendar.FRIDAY -> "Fri"
        java.util.Calendar.SATURDAY -> "Sat"
        else -> "Unknown Day"
    }
}