package com.nguyenminhkhang.taskmanagement.ui.home.action

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.nguyenminhkhang.taskmanagement.domain.model.ActionMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionDialog(
    items: List<ActionMenuItem>,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        ActionMenuList(
            items = items,
            onItemSelected = {
                it.onClick()
                onDismiss()
            }
        )
    }
}