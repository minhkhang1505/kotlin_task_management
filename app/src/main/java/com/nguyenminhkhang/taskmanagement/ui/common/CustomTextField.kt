package com.nguyenminhkhang.taskmanagement.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(content : String, textColor: Color, textSize: TextUnit, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier.padding(4.dp).fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = content,  modifier = Modifier.padding(horizontal = 4.dp), style = TextStyle(color = textColor, fontSize = textSize,))
            Icon(
                Icons.Default.Clear,
                contentDescription = "Clear",
                modifier = Modifier.padding(end = 4.dp).size(18.dp).clickable {
                    // Handle clear action
                    onClick()
                })
        }
    }
}