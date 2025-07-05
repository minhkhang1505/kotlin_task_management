package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatTopAppBar(
    onSave: () -> Unit,
    onNavigationBack: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.padding(start = 8.dp, end = 12.dp),
        title = { Text("Repeat") },
        navigationIcon = {
            Icon(
                Icons.Default.Clear, contentDescription = "Back",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onNavigationBack() }
            )
        },
        actions = {
            Text("Save",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onSave()
                        onNavigationBack()
                    }
            )
        }
    )
}