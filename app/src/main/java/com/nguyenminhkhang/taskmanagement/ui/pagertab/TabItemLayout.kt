package com.nguyenminhkhang.taskmanagement.ui.pagertab

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TabUiState

@Composable
fun TabItemLayout(state: TabUiState, isSelected: Boolean, onTabSelected: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(50))
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
            )
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onTabSelected
            )
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .zIndex(2f)
    ) {
        Text(
            text = state.title,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black
        )
    }
}

