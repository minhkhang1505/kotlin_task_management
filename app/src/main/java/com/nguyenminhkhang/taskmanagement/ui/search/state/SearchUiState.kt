package com.nguyenminhkhang.taskmanagement.ui.search.state

import com.nguyenminhkhang.shared.model.Task

data class SearchUiState(
    val todayTaskResult: List<Task> = emptyList(),
    val searchQuery: String = "",
    val isSearchBarVisible: Boolean = true,
    val expanded: Boolean = false
)