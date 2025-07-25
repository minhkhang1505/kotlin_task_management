package com.nguyenminhkhang.taskmanagement.ui.pagertab.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.home.HomeEvent
import com.nguyenminhkhang.taskmanagement.ui.home.state.HomeUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.TaskItemLayout
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskPageUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState

val itemBgColor = Color.White.copy(alpha = 0.5f)

fun LazyListScope.emptyState (
    key: String,state: TaskPageUiState
) {
    if(state.activeTaskList.isEmpty()) {
        item(key = key) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = itemBgColor
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val lottieComposition by rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(R.raw.lottie_empty_01)
                )
                LottieAnimation(lottieComposition)
                Text(
                    "Not have any tasks yet",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 30.dp),
                )
                Text("Add some tasks and follow it on Task Management Workspace",
                    fontSize = 14.sp, style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 50.dp, end = 50.dp, bottom = 30.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

fun LazyListScope.topCorner (key: String? = null) {
    item(key) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(
                    color = itemBgColor,
                    shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp)
                )
        )
    }
}

fun LazyListScope.bottomCorner(key: String? = null) {
    item(key) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(
                    color = itemBgColor,
                    shape = RoundedCornerShape(0.dp, 0.dp, 12.dp, 12.dp)
                )
        )
    }
}

fun LazyListScope.activeTasksHeader(key: String, state: TaskGroupUiState, onEvent: (HomeEvent) -> Unit) {
    item(key = key) {
        if( state.tab.id > 0) {
            Row(
                modifier = Modifier.background(color = itemBgColor,)
            ) {
                Text(
                    text = key, style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp, top = 8.dp, bottom = 8.dp), textAlign = TextAlign.Start
                )
                Icon(
                    painter = painterResource(R.drawable.baseline_filter_alt_24),
                    contentDescription = "Filter",
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                        .clickable {
                            onEvent(HomeEvent.RequestSortTasks(state.tab.id))
                        },
                    tint = MaterialTheme.colorScheme.primary
                )
                Icon(
                    Icons.Default.MoreVert, contentDescription = "More options",
                    modifier = Modifier
                        .padding(start = 8.dp, end = 6.dp, top = 8.dp, bottom = 8.dp)
                        .clickable {
                            onEvent(HomeEvent.UpdateCollectionRequested(state.tab.id))
                        },
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

fun LazyListScope.completeTasksHeader(key: String, state: TaskGroupUiState, onToggleExpand: () -> Unit) {
    if(state.page.completedTaskList.isNotEmpty()) {
        item(key = key) {
            if( state.tab.id > 0) {
                Row(
                    modifier = Modifier.background(color = itemBgColor).clickable { onToggleExpand() },
                ) {
                    Text(
                        text = "$key (${state.page.completedTaskList.size})", style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp), textAlign = TextAlign.Start
                    )
                    Icon(
                        Icons.Default.KeyboardArrowDown, contentDescription = "More options",
                        modifier = Modifier
                            .padding(start = 8.dp, end = 6.dp, top = 8.dp, bottom = 8.dp)
                            .clickable {

                            },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

fun LazyListScope.listTaskItems(taskState: HomeUiState, key: String, state : List<TaskUiState>, onEvent: (HomeEvent) -> Unit, navController: NavController) {
    itemsIndexed(
        state,
        key = { _, item -> "$key${item.id}" },
        contentType = { _, item -> item::class.java.name }) { _, item ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = itemBgColor)
        ) {
            TaskItemLayout(taskState= taskState,item, onEvent = onEvent, onTaskClick = {navController.navigate("TaskDetail/${it}") })
        }
    }
}

fun LazyListScope.spacer(height: Int) {
    item {
        Spacer(modifier = Modifier.height(height.dp))
    }
}