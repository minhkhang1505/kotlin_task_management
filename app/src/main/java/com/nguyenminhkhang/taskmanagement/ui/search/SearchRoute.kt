package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchRoute(
    onNavigateToTaskDetail: (Long) -> Unit,
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val searchState by viewModel.searchUiState.collectAsState()
    val results by viewModel.searchResults.collectAsStateWithLifecycle()

    SearchScreen(
        searchResults = results,
        searchUiState = searchState,
        onEvent = viewModel::onEvent,
        onNavigateToTaskDetail = onNavigateToTaskDetail,
        onScreenShow = viewModel::onScreenShow
    )
}