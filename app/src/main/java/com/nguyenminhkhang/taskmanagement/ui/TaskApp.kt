package com.nguyenminhkhang.taskmanagement.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import com.nguyenminhkhang.taskmanagement.ui.theme.TaskManagementTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nguyenminhkhang.taskmanagement.ui.home.HomeScreenRoute
import com.nguyenminhkhang.taskmanagement.ui.repeat.RepeatPage
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.TaskDetailPage

@Composable
fun TaskApp() {
    TaskManagementTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            startDestination = NavScreen.HOME.route
        ) {
            composable(route = NavScreen.HOME.route) { backStackEntry ->
                HomeScreenRoute(navController = navController, backStackEntry= backStackEntry)
            }

            composable(
                route = NavScreen.TASK_DETAIL.route,
                arguments = listOf(navArgument("taskId") { type = NavType.LongType })
            ) {
                TaskDetailPage(
                    navController = navController,
                )
            }

            composable(route = NavScreen.REPEAT.route,
                arguments = listOf(navArgument("taskId") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("taskId")
                RepeatPage(navController = navController)
            }
        }
    }
}

enum class NavScreen(val route: String) {
    HOME("Home"),
    TASK_DETAIL("TaskDetail/{taskId}"),
    REPEAT("Repeat/{taskId}"),
}