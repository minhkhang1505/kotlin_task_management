package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R

@Composable
fun TaskDetailInputRow(
    detailValue: String,
    onDetailChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(100.dp)
            .background(Color(0xFFCBE6F7), shape = RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Menu, contentDescription = "Menu Icon",
            modifier = Modifier
                .size(24.dp)
                .padding(start = 12.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        OutlinedTextField(
            value = detailValue,
            onValueChange = onDetailChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.detail_task_detail_descrip),
                    color = Color.Gray,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                          },
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
    Spacer(modifier = Modifier.height(8.dp))
}