package com.nguyenminhkhang.taskmanagement.ui.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.home.HomeEvent

@Composable
fun TopBar(onEvent: (HomeEvent) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 12.dp),
    ) {
        Text(
            text = "Task Management",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        IconButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(top = 18.dp),
            onClick = { onEvent(HomeEvent.ShowSearchBar) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Open Search"
            )
        }
    }
}