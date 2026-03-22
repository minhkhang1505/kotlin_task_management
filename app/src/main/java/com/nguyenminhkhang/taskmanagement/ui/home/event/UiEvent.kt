package com.nguyenminhkhang.taskmanagement.ui.home.event

sealed class UiEvent : HomeEvent {
    object ShowAddTaskSheet : UiEvent()
    object HideAddTaskSheet : UiEvent()
    object ShowAddDetailTextField : UiEvent()

    //Date picker
    object ShowDatePicker : UiEvent()
    object HideDatePicker : UiEvent()
    data class DateSelected(val date: Long) : UiEvent()

    //Time picker
    object ShowTimePicker : UiEvent()
    object HideTimePicker : UiEvent()
    data class TimeSelected(val time: Long) : UiEvent()

    // Add new collection sheet
    object ShowAddNewCollectionButton : UiEvent()
    object HideAddNewCollectionButton : UiEvent()

//    // Delete button
//    object ShowDeleteButton : UiEvent()
//    object HideDeleteButton : UiEvent()
    object OnToggleDeleteButton: UiEvent()
}