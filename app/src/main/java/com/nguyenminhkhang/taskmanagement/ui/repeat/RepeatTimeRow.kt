package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toHourMinuteString
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatUiState

@Composable
fun RepeatTimeRow(
    uiState: RepeatUiState,
    onShowTimePicker: () -> Unit,
    onClearDate: () -> Unit
) {
    Box(modifier = Modifier.padding(vertical = 6.dp).clickable { onShowTimePicker() }) {
        OutlinedTextField(
            value = uiState.task?.startTime?.toHourMinuteString() ?: "Select Time",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(
                    Icons.Default.Clear, contentDescription = "Clear",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onClearDate() }
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OUTLINETEXTFIELD_COLOR
        )
    }
}