@file:OptIn(ExperimentalMaterial3Api::class)

package com.nguyenminhkhang.taskmanagement.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DateDropDownMenu(
    selectedDay: Int?,
    onDaySelected: (Int) -> Unit,
) {
    val OUTLINETEXTFIELD_COLOR = OutlinedTextFieldDefaults.colors(
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = Color.Transparent,
        disabledBorderColor = MaterialTheme.colorScheme.outline,
        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    val days = (1..31).toList()
    var expendedListOfDayInMonth by remember { mutableStateOf(false) }
    val selectedDayInMonth = selectedDay?.toString() ?: "Select Date"

    ExposedDropdownMenuBox(
        expanded = expendedListOfDayInMonth,
        onExpandedChange = {expendedListOfDayInMonth = !expendedListOfDayInMonth}
    ) {
        OutlinedTextField(
            value = "Day $selectedDayInMonth",
            onValueChange = {},
            placeholder = { Text("Select Date") },
            trailingIcon = {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "Clear",
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            enabled = false,
            shape = RoundedCornerShape(12.dp),
            colors = OUTLINETEXTFIELD_COLOR
        )
        ExposedDropdownMenu(
            expanded = expendedListOfDayInMonth,
            onDismissRequest = { expendedListOfDayInMonth = false }
        ) {
            days.forEach { day ->
                DropdownMenuItem(
                    text = { Text(text = "Day $day") },
                    onClick = {
                        onDaySelected(day)
                        expendedListOfDayInMonth = false
                    }
                )
            }
        }
    }
}