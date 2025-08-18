package com.nguyenminhkhang.taskmanagement.ui.common.navigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.nguyenminhkhang.taskmanagement.ui.NavScreen

data class BottomDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
)

val BottomDestinations = listOf(
    BottomDestination(
        route = NavScreen.HOME.route,
        label = "Home",
        icon = Icons.Outlined.Home,
        contentDescription = "Home"
    ),
    BottomDestination(
        route = NavScreen.SEARCH.route,
        label = "Search",
        icon = Icons.Outlined.Search,
        contentDescription = "Search"
    ),
    BottomDestination(
        route = NavScreen.ACCOUNT.route,
        label = "Account",
        icon = Icons.Outlined.AccountCircle,
        contentDescription = "Account"
    ),

)