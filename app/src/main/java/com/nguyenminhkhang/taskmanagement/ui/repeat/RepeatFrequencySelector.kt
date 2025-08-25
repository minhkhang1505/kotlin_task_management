package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatUiState

@Composable
fun RepeatFrequencySelector(
    uiState: RepeatUiState,
    onEvent: (RepeatEvent) -> Unit,
) {
    Text(text = "Every")
    Row (
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
    ) {
        val focusManager = LocalFocusManager.current
        val density = LocalDensity.current

        OutlinedTextField(
            value = uiState.task?.repeatEvery.toString(),
            placeholder = { Text("1") },
            onValueChange = { onEvent(RepeatEvent.OnRepeatEveryChanged (it.toLongOrNull() ?: 1L))  },
            keyboardOptions = KeyboardOptions(keyboardType =  KeyboardType.Number, imeAction = ImeAction.Done),
            modifier = Modifier.weight(0.2f).padding(end = 8.dp),
            maxLines = 1,
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        Box (
            modifier = Modifier
                .onSizeChanged { size ->
                    uiState.textFieldWidth = with(density) { size.width.toDp() }
                }
                .clickable { onEvent(RepeatEvent.OnIntervalDropdownClicked) }
        ) {
            OutlinedTextField(
                value = uiState.task?.repeatInterval ?: "Week",
                onValueChange = {},
                trailingIcon = {
                    Icon(
                        Icons.Default.KeyboardArrowDown, contentDescription = "Dropdown",
                        modifier = Modifier.padding(8.dp)
                    )
                },
                readOnly = true,
                enabled = false,
                colors = OUTLINETEXTFIELD_COLOR,
            )
            DropdownMenu(
                expanded = uiState.isIntervalDropdownVisible,
                onDismissRequest = { onEvent(RepeatEvent.OnIntervalDropdownDismiss) },
                modifier = Modifier.width(uiState.textFieldWidth)
            ) {
                uiState.availableIntervals.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            onEvent(RepeatEvent.OnIntervalSelected(type))
                            onEvent(RepeatEvent.OnIntervalDropdownDismiss)
                        },
                    )
                }
            }
        }
    }
}