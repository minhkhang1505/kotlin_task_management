package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.search.state.SearchUiState

@Composable
fun TodayTaskList(
    searchUiState: SearchUiState,
    onEvent: (Long) -> Unit,
) {
    LazyColumn {
        items(searchUiState.todayTaskResult.size) { index ->
            val todayTask = searchUiState.todayTaskResult[index]
            SearchTaskItemLayout(
                taskResult = todayTask,
                onTaskClick = onEvent
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}