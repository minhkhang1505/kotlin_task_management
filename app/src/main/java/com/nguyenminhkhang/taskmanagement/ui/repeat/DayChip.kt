package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DayChip(
    day: String,
    setSelectedDayOfWeek: Set<String>,
    onDayClick: (String) -> Unit
) {
    var isSelected by remember { mutableStateOf(false) }
    isSelected = setSelectedDayOfWeek.contains(day)
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(45.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
            // Xử lý sự kiện click
            .clickable {
                onDayClick(day)
                isSelected = !isSelected
            } // Toggle selection state
    ) {
        Text(
            text = day,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}