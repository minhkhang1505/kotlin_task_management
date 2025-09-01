package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R

@Composable
fun TaskDetailTopAppBar (
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().size(56.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
            contentDescription = "Back",
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onNavigateBack() },
        )
        IconButton(
            onClick = { onFavoriteClick() },
            content = {
                Icon(
                    painter = painterResource(
                        id = if (isFavorite) {
                            R.drawable.baseline_star_24
                        } else {
                            R.drawable.baseline_star_outline_24
                        },
                    ),
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
            }
        )
    }
}