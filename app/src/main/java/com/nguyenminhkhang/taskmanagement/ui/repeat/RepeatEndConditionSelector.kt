package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.picker.convertMillisToDate
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatConstants
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatUiState

@Composable
fun RepeatEndConditionSelector(
    uiState: RepeatUiState,
    onEvent: (RepeatEvent) -> Unit,
) {
    Text(text = "End Condition", modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
    Column(
        modifier = Modifier.selectableGroup()
    ) {
        RepeatConstants.EndCondition.all.forEach { option ->
            Row(
                modifier = Modifier.padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == uiState.selectedEndCondition),
                    onClick = { onEvent(RepeatEvent.EndConditionChanged(option)) },
                )
                when (option) {
                    "Never" -> {
                        Text(
                            text = "Never",
                            modifier = Modifier.padding(end = 8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    "At" -> {
                        Row(
                            modifier = Modifier.selectable(
                                selected = ( "At" == uiState.selectedEndCondition),
                                onClick = {
                                    onEvent(RepeatEvent.EndConditionChanged("At"))
                                    onEvent(RepeatEvent.ShowEndDatePicker)
                                },
                                role = Role.RadioButton
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "On",
                                modifier = Modifier.padding(end = 8.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            OutlinedTextField(
                                value = uiState.task?.repeatEndDate?.let {
                                    convertMillisToDate(it)
                                } ?: "Select End Date",
                                placeholder = { Text("Select End Date") },
                                onValueChange = {},
                                readOnly = true,
                                enabled = false,
                                shape = RoundedCornerShape(12.dp),
                                colors = OUTLINETEXTFIELD_COLOR
                            )
                        }
                    }
                    "After" -> {
                        Row(
                            modifier = Modifier.selectable(
                                selected = ("After" == uiState.selectedEndCondition),
                                onClick = { onEvent(RepeatEvent.EndConditionChanged("After")) },
                                role = Role.RadioButton
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "After",
                                modifier = Modifier.padding(end = 8.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            val focusManager = LocalFocusManager.current
                            OutlinedTextField(
                                value = uiState.task?.repeatEndCount.toString(),
                                placeholder = { Text("1") },
                                onValueChange = { onEvent(RepeatEvent.EndConditionChanged(it)) },
                                modifier = Modifier.weight(0.2f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                enabled = ("After" == uiState.selectedEndCondition),
                                shape = RoundedCornerShape(12.dp),
                                colors = OUTLINETEXTFIELD_COLOR,
                                maxLines = 1,
                                singleLine = true,
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                            )
                            Text(
                                text = "occurrence",
                                modifier = Modifier.padding(end = 8.dp).weight(0.6f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}