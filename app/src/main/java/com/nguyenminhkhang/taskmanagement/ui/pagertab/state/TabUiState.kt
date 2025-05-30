package com.nguyenminhkhang.taskmanagement.ui.pagertab.state

import com.nguyenminhkhang.taskmanagement.database.entity.SortedType
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.toSortType

data class TabUiState(
    val id: Long,
    val title: String,
    val sortedType: SortedType
)

fun TaskCollection.toTabUiState(): TabUiState {
    return TabUiState(
        id = this.id ?: 0L,
        title = this.content,
        sortedType = this.sortedType.toSortType()
    )
}