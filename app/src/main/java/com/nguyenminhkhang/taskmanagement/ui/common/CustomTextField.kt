package com.nguyenminhkhang.taskmanagement.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(content : String, textColor: Color, textSize: TextUnit, onClick: () -> Unit = {}) {
    BoxWithConstraints(
        modifier = Modifier.padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = this.maxWidth
        Row(
            modifier = Modifier
                .widthIn(max = maxWidth) ,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = content,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 4.dp).weight(1f, fill = false),
                style = TextStyle(color = textColor, fontSize = textSize,))
            Icon(
                Icons.Default.Clear,
                contentDescription = "Clear",
                modifier = Modifier.size(18.dp).clickable {
                    // Handle clear action
                    onClick()
                })
            Spacer(modifier = Modifier.width(8.dp)) // Spacer to push the icon to the right
        }
    }
}