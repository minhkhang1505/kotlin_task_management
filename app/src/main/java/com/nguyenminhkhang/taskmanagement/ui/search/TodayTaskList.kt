package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.ui.search.state.SearchUiState

@Composable
fun TodayTaskList(
    searchUiState: SearchUiState,
    navController: NavController
) {
    LazyColumn {
        items(searchUiState.todayTaskResult.size) { index ->
            val todayTask = searchUiState.todayTaskResult[index]
            SearchTaskItemLayout(
                taskResult = todayTask,
                onTaskClick = {
                    navController.navigate("TaskDetail/${it}")
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}