package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
        Text("Search Page", style = MaterialTheme.typography.titleLarge )
        CustomSearchBar(
            searchResult = searchResults,
            searchState = searchUiState,
            onEvent = onEvent
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Today's Tasks",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (searchUiState.todayTaskResult.isEmpty()) {
            Text(
                text = "No tasks found",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            TodayTaskList(
                searchUiState = searchUiState,
                onEvent = {}
            )
        }
    }
}