package com.nguyenminhkhang.taskmanagement.ui.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchPage(
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val searchState by viewModel.searchUiState.collectAsState()
    val results by viewModel.searchResults.collectAsStateWithLifecycle()

    SearchLayout(
        searchResults = results,
        searchUiState = searchState,
        onEvent = viewModel::onEvent,
    )
}