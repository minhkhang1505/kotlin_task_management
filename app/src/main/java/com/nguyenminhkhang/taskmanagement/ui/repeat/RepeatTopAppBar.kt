package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RepeatTopAppBar(
    onSave: () -> Unit,
    onNavigationBack: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().size(56.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Clear, contentDescription = "Back",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onNavigationBack() }
            )
            Text(
                "Repeat",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
            )
        }
        Text("Save",
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    onSave()
                    onNavigationBack()
                }
        )
    }
}