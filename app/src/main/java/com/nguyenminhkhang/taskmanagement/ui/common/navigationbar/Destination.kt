package com.nguyenminhkhang.taskmanagement.ui.common.navigationbar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.navigation.AccountRoute
import com.nguyenminhkhang.taskmanagement.ui.navigation.HomeRoute
import com.nguyenminhkhang.taskmanagement.ui.navigation.SearchRoute
import kotlin.reflect.KClass

data class BottomDestination<T>(
    val route: T,
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
)

val BottomDestinations = listOf(
    BottomDestination(
        route = HomeRoute,
        label = R.string.bottom_bar_ic_home,
        icon = Icons.Outlined.Home,
        contentDescription = R.string.bottom_bar_ic_home
    ),
    BottomDestination(
        route = SearchRoute,
        label = R.string.bottom_bar_ic_search,
        icon = Icons.Outlined.Search,
        contentDescription = R.string.bottom_bar_ic_search
    ),
    BottomDestination(
        route = AccountRoute,
        label = R.string.bottom_bar_ic_account,
        icon = Icons.Outlined.AccountCircle,
        contentDescription = R.string.bottom_bar_ic_account
    ),
)