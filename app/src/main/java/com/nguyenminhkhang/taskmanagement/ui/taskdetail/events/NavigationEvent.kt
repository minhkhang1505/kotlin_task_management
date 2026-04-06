package com.nguyenminhkhang.taskmanagement.ui.taskdetail.events

sealed class NavigationEvent {
    data class NavigateBackWithResult(val taskId: Long) : NavigationEvent()
}