package com.nguyenminhkhang.taskmanagement.ui.common.pagertab.state

import com.nguyenminhkhang.shared.model.SortedType
import com.nguyenminhkhang.shared.model.Collection

data class TabUiState(
    val id: Long,
    val title: String,
    val sortedType: SortedType
)

fun Collection.toTabUiState(): TabUiState {
    return TabUiState(
        id = this.id ?: 0L,
        title = this.content,
        sortedType = this.sortedType
    )
}