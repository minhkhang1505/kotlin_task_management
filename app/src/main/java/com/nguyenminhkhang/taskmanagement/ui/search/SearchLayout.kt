package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.ui.search.state.SearchUiState

@Composable
fun SearchLayout(
    searchResults: List<TaskEntity>,
    searchUiState: SearchUiState,
    onEvent: (SearchEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        Text("Search Page", style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        CustomSearchBar(
            searchResult = searchResults,
            searchState = searchUiState,
            onEvent = onEvent
        )
    }
}