package com.nguyenminhkhang.taskmanagement.ui.common.navigationbar

import androidx.annotation.DrawableRes
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
    @DrawableRes val icon: Int,
    @StringRes val contentDescription: Int
)

val BottomDestinations = listOf(
    BottomDestination(
        route = HomeRoute,
        label = R.string.bottom_bar_ic_home,
        icon = R.drawable.ic_home,
        contentDescription = R.string.bottom_bar_ic_home
    ),
    BottomDestination(
        route = SearchRoute,
        label = R.string.bottom_bar_ic_search,
        icon = R.drawable.ic_search,
        contentDescription = R.string.bottom_bar_ic_search
    ),
    BottomDestination(
        route = AccountRoute,
        label = R.string.bottom_bar_ic_account,
        icon = R.drawable.ic_setting,
        contentDescription = R.string.bottom_bar_ic_account
    ),
)