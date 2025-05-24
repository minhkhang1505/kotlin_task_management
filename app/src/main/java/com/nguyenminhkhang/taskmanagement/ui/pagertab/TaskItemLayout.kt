package com.nguyenminhkhang.taskmanagement.ui.pagertab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState

@Composable
fun TaskItemLayout(
    state: TaskUiState,
    onCompletedTask: (TaskUiState) -> Unit = {},
    onFavoriteTask: (TaskUiState) -> Unit = {},
    onClickedTask: (TaskUiState) -> Unit = {}
){
    Row(
        modifier = Modifier.fillMaxWidth().clickable {
            onClickedTask(state)
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = state.isCompleted,
            onCheckedChange = {isChecked ->
                onCompletedTask(state)

            }
        )
        Text(
            text = state.content,
            modifier = Modifier.weight(1f),
            textDecoration = TextDecoration.LineThrough.takeIf { state.isCompleted }
        )
        if (state.isCompleted) {
            Icon(
                painter = painterResource(if(state.isFavorite) R.drawable.baseline_star_24 else R.drawable.baseline_star_outline_24),
                contentDescription = "Favorite Icon",
                modifier = Modifier.padding(end = 10.dp).clickable {
                    onFavoriteTask(state)
                }
            )
        }
    }
}