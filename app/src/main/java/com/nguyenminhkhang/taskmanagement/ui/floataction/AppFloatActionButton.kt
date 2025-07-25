package com.nguyenminhkhang.taskmanagement.ui.floataction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppFloatActionButton(
    clickable: Boolean = true,
    onClick: () -> Unit = {}
) {
    Box (
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.75f),
                spotColor = Color.Black.copy(alpha = 0.5f)
            )
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .size(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(clickable) {
                onClick.invoke()
            },
    ) {
        Text("+", style =  MaterialTheme.typography.titleLarge, color = Color.Black, modifier = Modifier.align(
            Alignment.Center))
    }
}