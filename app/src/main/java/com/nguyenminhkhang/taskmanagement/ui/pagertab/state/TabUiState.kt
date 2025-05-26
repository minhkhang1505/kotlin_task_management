package com.nguyenminhkhang.taskmanagement.ui.pagertab.state

import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection

data class TabUiState(
    val id: Long,
    val title: String
)

fun TaskCollection.toTabUiState(): TabUiState {
    return TabUiState(
        id = this.id ?: 0L,
        title = this.content
    )
}