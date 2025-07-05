package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatUiState

@Composable
fun WeekDaySelector(
    allDays: List<String>,
    selectedDays: Set<String>,
    onDayClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        allDays.forEach { day ->
            DayChip(
                day = day,
                setSelectedDayOfWeek = selectedDays,
                onDayClick = { onDayClick(day) }
            )
        }
    }
}