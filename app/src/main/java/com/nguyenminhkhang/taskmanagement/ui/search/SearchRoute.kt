package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchRoute(
    onNavigateToTaskDetail: (Long) -> Unit,
) {
    val viewModel: SearchViewModel = koinViewModel()
    val searchState by viewModel.searchUiState.collectAsStateWithLifecycle()
    val results by viewModel.searchResults.collectAsStateWithLifecycle()

    SearchScreen(
        searchResults = results,
        searchUiState = searchState,
        onEvent = viewModel::onEvent,
        onNavigateToTaskDetail = onNavigateToTaskDetail,
        onScreenShow = viewModel::onScreenShow
    )
}