package com.nguyenminhkhang.taskmanagement.ui.common.navigationbar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.NavScreen

data class BottomDestination(
    val route: String,
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
)

val BottomDestinations = listOf(
    BottomDestination(
        route = NavScreen.HOME.route,
        label = R.string.bottom_bar_ic_home,
        icon = Icons.Outlined.Home,
        contentDescription = R.string.bottom_bar_ic_home
    ),
    BottomDestination(
        route = NavScreen.SEARCH.route,
        label = R.string.bottom_bar_ic_search,
        icon = Icons.Outlined.Search,
        contentDescription = R.string.bottom_bar_ic_search
    ),
    BottomDestination(
        route = NavScreen.ACCOUNT.route,
        label = R.string.bottom_bar_ic_account,
        icon = Icons.Outlined.AccountCircle,
        contentDescription = R.string.bottom_bar_ic_account
    ),

)