package com.nguyenminhkhang.taskmanagement.ui.theme.topbar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(modifier: Modifier) {
    Box(
        modifier = modifier

    ) {
        Text(
            text = "Task Management",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .clickable {
                    Log.d("MainActivity", "Box clicked")
                }
                .align(Alignment.CenterEnd)
        ) {
            Text("T", modifier = Modifier.align(Alignment.Center))
        }
    }
}