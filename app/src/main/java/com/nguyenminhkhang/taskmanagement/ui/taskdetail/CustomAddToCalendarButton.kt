package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R

@Composable
fun AddToCalendarButton(
    onClick: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_calendar_month_24),
            contentDescription = "Add to Calendar",
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Add to Calendar",
        )
    }
}