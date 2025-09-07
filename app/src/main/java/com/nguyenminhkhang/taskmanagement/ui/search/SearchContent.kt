package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity

@Composable
fun SearchContent(searchResults: List<TaskEntity>, searchQuery: String, navController: NavController) {
    if (searchQuery.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(WindowInsets.ime.asPaddingValues()).padding(16.dp), contentAlignment = Alignment.TopCenter) {
            Text("Try searching for 'Project X' or 'Meeting'", style = MaterialTheme.typography.bodyMedium)
        }
    } else {
        val imePadding = WindowInsets.ime.asPaddingValues()

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = imePadding.calculateBottomPadding() + 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (searchResults.isEmpty()) {
                item {
                    Text(
                        text = "No tasks found",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                items(searchResults, key = { task -> task.id ?: 0 }) { result ->
                    SearchTaskItemLayout(
                        taskResult = result,
                        onTaskClick = {navController.navigate("TaskDetail/${it}") }
                    )
                }
            }
        }
    }
}