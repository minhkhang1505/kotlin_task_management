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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.TaskDelegate
import com.nguyenminhkhang.taskmanagement.ui.pagertab.TaskItemLayout
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskPageUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState

val itemBgColor = Color.Black.copy(alpha = 0.1f)

fun LazyListScope.emptyState (
    key: String,state: TaskPageUiState
) {
    if(state.activeTaskList.isEmpty()) {
        item(key = key) {
            Column(
                modifier = Modifier
                    .fillMaxWidth().background(
                        color = Color.Black.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val lottieComposition by rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(R.raw.lottie_empty_01)
                )
                LottieAnimation(lottieComposition)
                Text(
                    "All tasks completed",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text("Nice work!", fontSize = 14.sp, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

fun LazyListScope.topCorner (key: String? = null) {
    item(key) {
        Box(
            modifier = Modifier.fillMaxWidth().height(12.dp).background(
                color = itemBgColor,
                shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp)
            )
        )
    }
}

fun LazyListScope.bottomCorner(key: String? = null) {
    item(key) {
        Box(
            modifier = Modifier.fillMaxWidth().height(12.dp).background(
                color = itemBgColor,
                shape = RoundedCornerShape(0.dp, 0.dp, 12.dp, 12.dp)
            )
        )
    }
}

fun LazyListScope.activeTasksHeader(key: String, state: TaskGroupUiState, taskDelegate: TaskDelegate) {
    if(state.page.activeTaskList.isNotEmpty()) {
        item(key = key) {
            if( state.tab.id > 0) {
                Row(
                    modifier = Modifier.background(color = Color.Black.copy(alpha = 0.1f),)
                ) {
                    Text(
                        text = key, style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f).padding(start = 12.dp), textAlign = TextAlign.Start
                    )
                    Icon(
                        painter = painterResource(R.drawable.baseline_filter_alt_24),
                        contentDescription = "Filter",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                taskDelegate.requestSortTasks(state.tab.id)
                            },
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        Icons.Default.MoreVert, contentDescription = "More options",
                        modifier = Modifier
                            .padding(start = 8.dp, end = 6.dp)
                            .clickable {
                                taskDelegate.requestUpdateCollection(state.tab.id)
                            },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

fun LazyListScope.completeTasksHeader(key: String, state: TaskGroupUiState) {
    if(state.page.activeTaskList.isNotEmpty()) {
        item(key = key) {
            if( state.tab.id > 0) {
                Row(
                    modifier = Modifier.background(color = Color.Black.copy(alpha = 0.1f),)
                ) {
                    Text(
                        text = "$key (${state.page.completedTaskList.size})", style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f).padding(start = 12.dp), textAlign = TextAlign.Start
                    )
                    Icon(
                        Icons.Default.KeyboardArrowDown, contentDescription = "More options",
                        modifier = Modifier
                            .padding(start = 8.dp, end = 6.dp)
                            .clickable {

                            },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

fun LazyListScope.listTaskItems( key: String, state : List<TaskUiState>, taskDelegate: TaskDelegate) {
    itemsIndexed(state, key = { _, item -> "$key${item.id}" }, contentType = { _, item -> item::class.java.name }) { _, item ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black.copy(alpha = 0.1f)) // Add your desired background color here
        ) {
            TaskItemLayout(item, taskDelegate)
        }
    }
}

fun LazyListScope.spacer(height: Int) {
    item {
        Spacer(modifier = Modifier.height(height.dp))
    }
}