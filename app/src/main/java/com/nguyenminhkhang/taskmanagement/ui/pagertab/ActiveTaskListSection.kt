package com.nguyenminhkhang.taskmanagement.ui.pagertab

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState

@Composable
fun ActiveTaskListSection(collectionId: Long,activeTaskList: List<TaskUiState>, taskDelegate: TaskDelegate) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedVisibility(visible = activeTaskList.isNotEmpty()) {
            if( collectionId > 0) {
                Row(
                    modifier = Modifier.padding(start = 12.dp, end = 6.dp)
                ) {
                    Text(
                        text = "Title", style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f), textAlign = TextAlign.Start
                    )
                    Icon(
                        painter = painterResource(R.drawable.baseline_filter_alt_24),
                        contentDescription = "Filter",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                taskDelegate.requestSortTasks(collectionId)
                            },
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        Icons.Default.MoreVert, contentDescription = "More options",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                taskDelegate.requestUpdateCollection(collectionId)
                            },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = activeTaskList.isEmpty(),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
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

        activeTaskList.forEach {
            TaskItemLayout( it,onCompletedTask = {
                taskDelegate.invertTaskCompleted(it)
                Log.d("TaskItemLayout", "onCompletedTask: $it")
            }, onFavoriteTask = {
                taskDelegate.invertTaskFavorite(it)
                Log.d("TaskItemLayout", "onFavoriteTask: $it")
            }, onClickedTask = {
                taskDelegate.invertTaskCompleted(it)
                Log.d("TaskItemLayout", "onClickedTask: $it")
            })
        }
    }
}