package com.nguyenminhkhang.taskmanagement.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nguyenminhkhang.taskmanagement.ui.theme.TaskManagementTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nguyenminhkhang.taskmanagement.ui.home.HomeLayout

@Composable
fun TaskApp() {
    TaskManagementTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            startDestination = NavScreen.HOME.route
        ) {
            composable(route = NavScreen.HOME.route) {
                HomeLayout()
            }
        }
    }
}

enum class NavScreen(val route: String) {
    HOME("Home"),
    TASK("task/{taskId}"),
}