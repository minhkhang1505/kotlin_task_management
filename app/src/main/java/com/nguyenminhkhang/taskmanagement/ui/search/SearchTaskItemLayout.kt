package com.nguyenminhkhang.taskmanagement.ui.search

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.millisToDateString

@Composable
fun SearchTaskItemLayout(
    taskResult: TaskEntity,
    onTaskClick: (Long) -> Unit,
    onEvent:(SearchEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .heightIn(min = 56.dp)
            .background(
                color = MaterialTheme.colorScheme.onPrimary.copy(0.7f),
                shape = RoundedCornerShape(12.dp)
            ).
            clickable( onClick = {
                onEvent(SearchEvent.OnSearchResultClick(taskResult.id!!))
                onTaskClick(taskResult.id!!)
                Log.d("SearchTaskItemLayout", "Task clicked: ${taskResult.id}")
            }
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column (
            modifier = Modifier.padding(start = 16.dp)
        ){
            Text(
                text = taskResult.content,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text ="Due: ${taskResult.repeatEndDate?.millisToDateString() ?: "No due date"}",
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Icon(
            painter = painterResource(if(taskResult.favorite) R.drawable.baseline_star_24 else R.drawable.baseline_star_outline_24),
            contentDescription = "Favorite Icon",
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            modifier = Modifier.padding(end = 10.dp).clickable {
                onEvent(SearchEvent.OnToggleFavoriteClick(taskResult.id!!, !taskResult.favorite))
            }
        )
    }
}