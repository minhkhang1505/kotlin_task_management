package com.nguyenminhkhang.taskmanagement.ui.pagertab

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nguyenminhkhang.taskmanagement.ui.home.TaskDelegate
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState

@Composable
fun CompletedTaskListSection(completedTask: List<TaskUiState>, taskDelegate: TaskDelegate) {
    var isExpanded by remember { mutableStateOf(false) }
    LazyColumn (
        modifier = Modifier.fillMaxWidth()
            .background(
                color = Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Row(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { isExpanded = !isExpanded }
            ) {
                Text(
                    text = "Completed (${completedTask.size})",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 14.dp, end = 6.dp)
                )
                if (!isExpanded) Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    modifier = Modifier.padding(end = 12.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (isExpanded) {
            items(completedTask) { task ->
//                TaskItemLayout(
//                    task,
//                    taskDelegate = taskDelegate,
//                )
            }
        }

    }
}