package com.nguyenminhkhang.taskmanagement.ui.account

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nguyenminhkhang.taskmanagement.R

@Composable
fun Avatar(
    size: Dp = 60.dp,
    imageUrl: String? = null,
    initials: String = "",
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.clip(CircleShape).border(2.dp, color = MaterialTheme.colorScheme.primary, shape = CircleShape).size(size),
        contentAlignment = Alignment.Center
    ) {
        when {
            !imageUrl.isNullOrBlank() -> {
                AsyncImage(
                    model = remember(imageUrl) {
                        ImageRequest.Builder(context)
                            .data(imageUrl)
                            .crossfade(true)
                            .build()
                    },
                    contentDescription = "User Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize().clip(CircleShape).padding(2.dp),
                    placeholder = androidx.compose.ui.res.painterResource(R.drawable.baseline_person_24),
                    error = androidx.compose.ui.res.painterResource(R.drawable.baseline_person_24)
                )
            }
            initials.isNotBlank() -> {
                Text(
                    text = initials.take(2).uppercase(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = (size.value * 0.4).sp,
                    textAlign = TextAlign.Center
                )
            }
            else -> {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.baseline_person_24)
                        .build(),
                    contentDescription = "Default Avatar",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.matchParentSize().clip(CircleShape).padding(2.dp)
                )
            }
        }
    }
}