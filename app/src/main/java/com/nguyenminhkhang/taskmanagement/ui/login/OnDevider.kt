package com.nguyenminhkhang.taskmanagement.ui.login

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OrDivider() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(0.6f)
    ) {
        Divider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = Color.Gray
        )
        Text(
            text = "OR",
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Divider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = Color.Gray
        )
    }
}
