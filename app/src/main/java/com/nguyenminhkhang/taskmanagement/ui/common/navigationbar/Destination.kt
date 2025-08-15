package com.nguyenminhkhang.taskmanagement.ui.common.navigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String,
) {
    Home(
        route = "home",
        icon = Icons.Default.Home,
        label = "Home",
        contentDescription = "Home",
    ),
    Account(
        route = "account",
        icon = Icons.Default.AccountBox,
        label = "Account",
        contentDescription = "Account",
    )
}