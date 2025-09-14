package com.nguyenminhkhang.taskmanagement.ui.pagertab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.home.HomeEvent
import com.nguyenminhkhang.taskmanagement.ui.home.state.HomeUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.millisToDateString

@Composable
fun LazyItemScope.TaskItemLayout(
    taskState: HomeUiState,
    state: TaskUiState,
    onEvent: (HomeEvent)-> Unit,
    onTaskClick: (Long) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                onTaskClick(state.id!!)
            }
            .animateItem(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = state.completed,
            onCheckedChange = {isChecked ->
                onEvent(HomeEvent.ToggleComplete(state))
            }
        )
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = state.content,
                modifier = Modifier.padding(horizontal = 4.dp),
                textDecoration = TextDecoration.LineThrough.takeIf { state.completed }
            )
            if(state.completed) {
                Text(text = "Completed: ${state.stringUpdateAt}", modifier = Modifier.padding(end = 10.dp), color = MaterialTheme.colorScheme.primary)
            } else {
                if(state.repeatEndDate != null && state.startDate != null) {
                    Text(
                        text = "Begin: ${state.startDate!!.millisToDateString()}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 10.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                }
            }
        }

        if (!state.completed) {
            Icon(
                painter = painterResource(if(state.isFavorite) R.drawable.baseline_star_24 else R.drawable.baseline_star_outline_24),
                contentDescription = "Favorite Icon",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                modifier = Modifier.padding(end = 10.dp).clickable {
                    onEvent(HomeEvent.ToggleFavorite(state))
                }
            )
        }
        if (taskState.isShowDeleteButtonVisible) {
            Icon(
                painter = painterResource(R.drawable.baseline_delete_outline_24),
                contentDescription = "Delete Icon",
                modifier = Modifier.padding(end = 10.dp).clickable {
                    onEvent(HomeEvent.DeleteTask(state.id!!))
                }
            )
        }
    }
}