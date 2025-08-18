package com.nguyenminhkhang.taskmanagement.ui.search.state

data class SearchUiState(
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val searchResults: List<String> = emptyList(), // Replace String with your actual data type
    val errorMessage: String? = null
)
