package com.nguyenminhkhang.taskmanagement.ui.home.sort

import com.nguyenminhkhang.taskmanagement.R
import com.nguyeminhkhang.shared.model.SortMenuItem
import com.nguyeminhkhang.shared.model.SortedType
import com.nguyenminhkhang.taskmanagement.ui.common.stringprovider.StringProvider
import com.nguyenminhkhang.taskmanagement.ui.home.event.HomeEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.MenuEvent

fun buildSortMenuItems(
    strings: StringProvider,
    collectionId: Long,
    onEvent: (HomeEvent) -> Unit
) : List<SortMenuItem> {
    return listOf(
        SortMenuItem(
            title = strings.getString(R.string.sort_by_date),
            sortedType = SortedType.SORTED_BY_DATE,
        ) {
            onEvent(MenuEvent.SortCollection(collectionId, SortedType.SORTED_BY_DATE))
        },
        SortMenuItem(
            title = strings.getString(R.string.sort_by_favorite),
            sortedType = SortedType.SORTED_BY_FAVORITE,
        ) {
            onEvent(MenuEvent.SortCollection(collectionId, SortedType.SORTED_BY_FAVORITE))
        }
    )
}