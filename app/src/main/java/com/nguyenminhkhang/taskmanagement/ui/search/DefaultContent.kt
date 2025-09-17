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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.search.state.SearchUiState

@Composable
fun DefaultContent(
    todayTasks: SearchUiState,
    navController: NavController,
    onEvent: (SearchEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp), // Tăng padding top một chút
    ) {
        Text(
            text = stringResource(R.string.today_task),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (todayTasks.todayTaskResult.isEmpty()) {
            Text(
                text = stringResource(R.string.today_task_empty),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            TodayTaskList(searchUiState = todayTasks, navController = navController, onEvent = onEvent)
        }
    }
}