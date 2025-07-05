package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailTopAppBar (
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "", style = MaterialTheme.typography.titleLarge) },
        actions = {
            IconButton(
                onClick = { onFavoriteClick() },
                content = {
                    Icon(
                        painter = painterResource(
                            id = if (isFavorite) {
                                R.drawable.baseline_star_24
                            } else {
                                R.drawable.baseline_star_outline_24
                            }
                        ),
                        contentDescription = "Favorite"
                    )
                }
            )
        },
        navigationIcon = {
            Icon(
                imageVector =  Icons.Default.Clear,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onNavigateBack() },
            )
        }
    )
}