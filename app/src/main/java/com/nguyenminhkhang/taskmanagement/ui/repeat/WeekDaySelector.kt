package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.common.components.DayChip
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatConstants

@Composable
fun WeekDaySelector(
    selectedDays: List<String>,
    onDayClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        RepeatConstants.DateItems.all.forEach { day ->
            DayChip(
                day = day,
                isSelected = selectedDays.contains(day),
                onDayClick = { onDayClick(day) }
            )
        }
    }
}