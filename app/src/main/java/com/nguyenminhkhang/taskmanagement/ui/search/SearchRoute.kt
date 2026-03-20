package com.nguyenminhkhang.taskmanagement.ui.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
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
        onNavigateToTaskDetail = onNavigateToTaskDetail
    )
}