package com.nguyenminhkhang.taskmanagement.ui.home.action

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.domain.model.ActionMenuItem

@Composable
fun ActionMenuList(
    items: List<ActionMenuItem>,
    onItemSelected: (ActionMenuItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            Text(
                text = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clickable { onItemSelected(item) }
            )
        }
    }
}