package com.nguyenminhkhang.taskmanagement.ui.pagertab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.TaskDelegate
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskPageUiState

@Composable
fun TaskListPage(state: TaskPageUiState, taskDelegate: TaskDelegate) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ActiveTaskListSection(state.activeTaskList, taskDelegate)
        CompletedTaskListSection(state.completedTaskList, taskDelegate)
    }
}