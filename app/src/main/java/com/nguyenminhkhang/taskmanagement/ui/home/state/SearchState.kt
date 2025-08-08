package com.nguyenminhkhang.taskmanagement.ui.home.state

data class SearchState(
    val searchQuery: String = "",
    val isSearchBarVisible: Boolean = false,
    val expanded: Boolean = false
)