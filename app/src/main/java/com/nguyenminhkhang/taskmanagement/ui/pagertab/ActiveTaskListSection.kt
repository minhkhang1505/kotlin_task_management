package com.nguyenminhkhang.taskmanagement.ui.pagertab

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.home.TaskDelegate

@Composable
fun ActiveTaskListSection(collectionId: Long,activeTaskList: List<TaskUiState>, taskDelegate: TaskDelegate) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 500.dp)
            .background(
                color = Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//        AnimatedVisibility(visible = activeTaskList.isNotEmpty()) {
//            if( collectionId > 0) {
//                Row(
//                    modifier = Modifier.padding(start = 12.dp, end = 6.dp)
//                ) {
//                    Text(
//                        text = "Title", style = MaterialTheme.typography.titleLarge,
//                        modifier = Modifier.weight(1f), textAlign = TextAlign.Start
//                    )
//                    Icon(
//                        painter = painterResource(R.drawable.baseline_filter_alt_24),
//                        contentDescription = "Filter",
//                        modifier = Modifier
//                            .padding(start = 8.dp)
//                            .clickable {
//                                taskDelegate.requestSortTasks(collectionId)
//                            },
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                    Icon(
//                        Icons.Default.MoreVert, contentDescription = "More options",
//                        modifier = Modifier
//                            .padding(start = 8.dp)
//                            .clickable {
//                                taskDelegate.requestUpdateCollection(collectionId)
//                            },
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                }
//            }
//        }

//        activeTaskList.forEach {
//            TaskItemLayout( it,onCompletedTask = {
//                taskDelegate.invertTaskCompleted(it)
//                Log.d("TaskItemLayout", "onCompletedTask: $it")
//            }, onFavoriteTask = {
//                taskDelegate.invertTaskFavorite(it)
//                Log.d("TaskItemLayout", "onFavoriteTask: $it")
//            }, onClickedTask = {
//                taskDelegate.invertTaskCompleted(it)
//                Log.d("TaskItemLayout", "onClickedTask: $it")
//            })
//        }
    }
}