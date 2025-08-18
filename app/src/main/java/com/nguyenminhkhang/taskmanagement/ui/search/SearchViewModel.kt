package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.lifecycle.ViewModel
import com.nguyenminhkhang.taskmanagement.ui.search.state.SearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel : ViewModel() {
    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState = _searchUiState.asStateFlow()
}