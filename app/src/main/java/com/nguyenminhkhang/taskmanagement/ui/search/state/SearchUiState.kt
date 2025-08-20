package com.nguyenminhkhang.taskmanagement.ui.search.state

import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity

data class SearchUiState(
    val todayTaskResult: List<TaskEntity> = emptyList(),
    val searchQuery: String = "",
    val isSearchBarVisible: Boolean = true,
    val expanded: Boolean = false
)