package com.nguyenminhkhang.taskmanagement.ui.floataction

import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ID_ADD_FAVORITE_LIST
import com.nguyenminhkhang.taskmanagement.TaskDelegate

@Composable
fun AppFloatActionButton(
    clickable: Boolean = true,
    onClick: () -> Unit = {}
) {
    Box (
        modifier = Modifier
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(16.dp)
            )
            .size(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(clickable) {
                onClick.invoke()
            },
    ) {
        Text("+", style =  MaterialTheme.typography.titleLarge, color = Color.White, modifier = Modifier.align(
            Alignment.Center))
    }
}