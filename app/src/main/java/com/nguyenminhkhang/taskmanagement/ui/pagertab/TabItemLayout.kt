package com.nguyenminhkhang.taskmanagement.ui.pagertab

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TabUiState
import java.lang.reflect.Modifier

@Composable
fun TabItemLayout(state : TabUiState, isSelected: Boolean, onTabSelected: () -> Unit) {
    Tab(text = {Text(state.title)}, selected = isSelected, onClick = onTabSelected)
}