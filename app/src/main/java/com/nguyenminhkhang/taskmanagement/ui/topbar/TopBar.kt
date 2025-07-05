package com.nguyenminhkhang.taskmanagement.ui.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = "Task Management",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}