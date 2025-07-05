package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.datepicker.convertMillisToDate
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatUiState

@Composable
fun RepeatStartDateRow(
    uiState: RepeatUiState,
    onShowDatePicker: () -> Unit,
    onClearDate: () -> Unit
) {
    Text(text = "Start Date", modifier = Modifier.padding(vertical = 8.dp))
    Box(modifier = Modifier.clickable { onShowDatePicker() }
    ) {
        OutlinedTextField(
            value = uiState.task?.startDate?.let { convertMillisToDate(it) } ?: "Select Start Date",
            placeholder = { Text("Select Start Date") },
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            readOnly = true,
            enabled = false,
            trailingIcon = {
                if (uiState.task?.startDate != null) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Calendar Icon",
                        modifier = Modifier.padding(8.dp).clickable(onClick = onClearDate)
                    )
                }
            },
            colors = OUTLINETEXTFIELD_COLOR
        )
    }
}