package com.nguyenminhkhang.taskmanagement.ui.home.sort

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyeminhkhang.shared.model.SortMenuItem
import com.nguyenminhkhang.taskmanagement.ui.common.bottomsheet.BottomSheetItem

@Composable
fun SortMenuList (
    items: List<SortMenuItem>,
    onItemSelected: (SortMenuItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Sort by",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp),
        )
        items.forEach { item ->
            BottomSheetItem(
                title = item.title,
                onClick = { onItemSelected(item) }
            )
        }
    }
}