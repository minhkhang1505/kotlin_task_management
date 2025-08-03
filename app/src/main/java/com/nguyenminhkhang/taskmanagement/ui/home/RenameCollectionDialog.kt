package com.nguyenminhkhang.taskmanagement.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun RenameCollectionDialog(
    newCollectionName: String,
    onEvent: (HomeEvent) -> Unit
) {
    Dialog(
        onDismissRequest = {
            onEvent(HomeEvent.HideRenameCollectionDialog)
        }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Rename Collection",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )

                TextField(
                    value = newCollectionName,
                    onValueChange = {
                        onEvent(HomeEvent.OnCollectionNameChange(it))
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,    // Tắt gạch chân khi focus
                        unfocusedIndicatorColor = Color.Transparent,  // Tắt gạch chân khi không focus
                        disabledIndicatorColor = Color.Transparent,   // Tắt gạch chân khi bị vô hiệu hóa
                        errorIndicatorColor = Color.Transparent       // Tắt gạch chân khi có lỗi
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            onEvent(HomeEvent.HideRenameCollectionDialog)
                        }
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onEvent(HomeEvent.RenameCollection(newCollectionName))
                            onEvent(HomeEvent.HideRenameCollectionDialog)
                        }
                    ) {
                        Text("Rename")
                    }
                }
            }
        }
    }
}
