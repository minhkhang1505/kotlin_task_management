package com.nguyenminhkhang.taskmanagement.ui.common.navigationbar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun NavigationBottomBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDest = backStackEntry?.destination
    NavigationBar {
        BottomDestinations.forEach { dest ->
            val selected = currentDest?.hierarchy?.any { it.route == dest.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(dest.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(dest.icon, contentDescription = stringResource(dest.contentDescription)) },
                label = { Text(stringResource(dest.label)) }
            )
        }
    }
}