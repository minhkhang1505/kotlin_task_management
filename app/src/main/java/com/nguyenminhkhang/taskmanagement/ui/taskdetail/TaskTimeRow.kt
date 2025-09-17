package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.common.CustomTextField
import com.nguyenminhkhang.taskmanagement.ui.common.RoundedOutlinedTextField
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toHourMinuteString
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.state.TaskDetailScreenUiState

@Composable
fun TaskTimeRow(
    uiState: TaskDetailScreenUiState,
    onShowDatePicker: () -> Unit,
    onClearDate: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onShowDatePicker() }
            .background(Color(0xFFF3E0CD), shape = RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_access_time_24),
                contentDescription = "Time Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp)),
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (uiState.task?.startTime == null) {
                Text(
                    text = stringResource(R.string.detail_add_start_time_descrip), modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            } else {
                CustomTextField(
                    content = uiState.task.startTime.toHourMinuteString(),
                    onClick = { onClearDate() },
                    textColor = Color.Black, // Brown color for time text
                    textSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
        }
    }
}