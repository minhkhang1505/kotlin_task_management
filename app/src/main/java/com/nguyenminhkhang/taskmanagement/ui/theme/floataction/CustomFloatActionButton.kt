package com.nguyenminhkhang.taskmanagement.ui.theme.floataction

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomFloatActionButton(
    modifier: Modifier,
    clickable: Boolean = true,
    onClick: () -> Unit = {}
) {
    Box (
        modifier = modifier.clickable(clickable) {
            onClick.invoke()
        },
    ) {
        Text("+", style = MaterialTheme.typography.titleLarge, color = Color.White, modifier = Modifier.align(
            Alignment.Center))
    }
}