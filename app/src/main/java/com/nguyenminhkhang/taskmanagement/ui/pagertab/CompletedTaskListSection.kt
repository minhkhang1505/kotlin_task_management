package com.nguyenminhkhang.taskmanagement.ui.pagertab

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.TaskDelegate
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState

@Composable
fun CompletedTaskListSection(completedTask: List<TaskUiState>, taskDelegate: TaskDelegate) {
    Column (
        modifier = Modifier.fillMaxWidth()
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
        completedTask.forEach {
            TaskItemLayout(it, onCompletedTask = {
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