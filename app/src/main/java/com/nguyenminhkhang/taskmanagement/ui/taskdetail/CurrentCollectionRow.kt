package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.state.TaskDetailScreenUiState
import kotlinx.coroutines.launch

@Composable
fun CurrentCollectionRow(
    collectionName: String,
    onCollectionClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable{ onCollectionClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = collectionName,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp),
            color = Color.Gray
        )
        Icon(
            painter = painterResource(R.drawable.baseline_arrow_drop_down_24),
            contentDescription = "Task Icon",
            modifier = Modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelButtonChangeCollection(
    uiState: TaskDetailScreenUiState,
    onEvent: (TaskDetailEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = {
            onEvent(TaskDetailEvent.CloseChangeCollectionSheet)
        },
        sheetState = sheetState,
    ) {
        Text(
            text = "Select Collection",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp),
        )
        uiState.collection.forEach { collection ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp))
                    .clickable {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onEvent(TaskDetailEvent.CurrentCollectionChanged(collection.id!!))
                            }
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = collection.content, modifier = Modifier.padding(horizontal = 16.dp))
            }
//            Button(
//                onClick = {
//
//                },
//                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp).fillMaxWidth(),
//            ) {
//
//            }
        }
    }
}