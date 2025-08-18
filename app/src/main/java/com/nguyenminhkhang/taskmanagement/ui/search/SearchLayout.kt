package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nguyenminhkhang.taskmanagement.ui.search.state.SearchUiState

@Composable
fun SearchLayout(
    searchUiState: SearchUiState
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Search Page", style = MaterialTheme.typography.headlineLarge)
    }
}