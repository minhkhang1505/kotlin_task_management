package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun TaskDetailInputRow(
    detailValue: String,
    onDetailChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Menu, contentDescription = "Menu Icon",
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        OutlinedTextField(
            value = detailValue,
            onValueChange = onDetailChange,
            placeholder = { Text(text = "Add some detail") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
                .border(
                    width = 0.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    defaultKeyboardAction(ImeAction.Done)
                    onSave()
                }
            )
        )
    }
}