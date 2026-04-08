package com.nguyenminhkhang.taskmanagement.ui.home.event

import com.nguyenminhkhang.shared.model.SortedType
import com.nguyenminhkhang.taskmanagement.ui.home.AppMenuItem

sealed class MenuEvent : HomeEvent {
    object ResetMenuListButtonSheet : MenuEvent()
    data class RequestShowButtonSheetOption(val list: List<AppMenuItem>) : MenuEvent()

    // Sort
    data class SortCollection(val collectionId: Long, val sortedType: SortedType) : MenuEvent()
    object DismissSortDialog : MenuEvent()
    object ShowSortDialog : MenuEvent()
    object DismissActionBottomSheet: MenuEvent()
    object ShowActionBottomSheet: MenuEvent()
}