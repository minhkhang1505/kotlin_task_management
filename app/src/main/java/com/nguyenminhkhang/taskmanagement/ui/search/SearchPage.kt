package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchPage(
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val searchState by viewModel.searchUiState.collectAsState()
    SearchLayout(
        searchUiState = searchState
    )
}