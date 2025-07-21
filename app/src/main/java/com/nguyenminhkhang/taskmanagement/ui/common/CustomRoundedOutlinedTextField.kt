package com.nguyenminhkhang.taskmanagement.ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RoundedOutlinedTextField(content : String, onClick: () -> Unit = {}) {

    Box(
        modifier = Modifier.border(width = 1.dp,
            color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(8.dp)
        ).padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = content, modifier = Modifier.padding(horizontal = 4.dp))
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