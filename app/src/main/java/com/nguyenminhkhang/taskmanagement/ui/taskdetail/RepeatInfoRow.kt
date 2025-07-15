package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.common.RoundedOutlinedTextField


@Composable
fun RepeatInfoRow(
    summaryText: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(R.drawable.baseline_repeat_24),
            contentDescription = "Time Icon",
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(12.dp)),
        )
        Spacer(modifier = Modifier.width(8.dp))
        if(summaryText.isNotBlank()) {
            RoundedOutlinedTextField(
                content = summaryText,
                onClick = onClick,
            )
        } else {
            Text(
                text = "Set repeat times",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}