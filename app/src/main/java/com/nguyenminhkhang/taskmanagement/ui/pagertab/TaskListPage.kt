package com.nguyenminhkhang.taskmanagement.ui.pagertab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.TaskDelegate
import com.nguyenminhkhang.taskmanagement.ui.pagertab.items.activeTasksHeader
import com.nguyenminhkhang.taskmanagement.ui.pagertab.items.bottomCorner
import com.nguyenminhkhang.taskmanagement.ui.pagertab.items.completeTasksHeader
import com.nguyenminhkhang.taskmanagement.ui.pagertab.items.emptyState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.items.listTaskItems
import com.nguyenminhkhang.taskmanagement.ui.pagertab.items.spacer
import com.nguyenminhkhang.taskmanagement.ui.pagertab.items.topCorner
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState

@Composable
fun TaskListPage(state: TaskGroupUiState, taskDelegate: TaskDelegate) {
    var isExpandedCompletedTask by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        if(state.page.activeTaskList.isNotEmpty()) {
            topCorner()
        }
        activeTasksHeader(state.tab.title, state, taskDelegate)
        emptyState("empty", state.page)
        listTaskItems("active", state.page.activeTaskList, taskDelegate)
        if(state.page.activeTaskList.isNotEmpty()) {
            bottomCorner()
        }

        spacer(12)
        if(state.page.completedTaskList.isNotEmpty()) {
            topCorner()
            completeTasksHeader("Completed", state, onToggleExpand = { isExpandedCompletedTask = !isExpandedCompletedTask })
            if( isExpandedCompletedTask) {
                listTaskItems("completed", state.page.completedTaskList, taskDelegate)
            }
            bottomCorner()
        }
    }
}