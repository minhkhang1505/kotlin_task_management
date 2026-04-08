package com.nguyenminhkhang.taskmanagement.ui.home.sort

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.nguyenminhkhang.shared.model.SortMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortModalBottomSheet(
    items: List<SortMenuItem>,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        SortMenuList(
            items = items,
            onItemSelected = {
                it.onClick()
                onDismiss()
            }
        )
    }
}