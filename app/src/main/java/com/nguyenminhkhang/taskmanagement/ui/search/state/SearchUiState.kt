package com.nguyenminhkhang.taskmanagement.ui.search.state

data class SearchUiState(
    val searchQuery: String = "",
    val isSearchBarVisible: Boolean = true,
    val expanded: Boolean = false
)