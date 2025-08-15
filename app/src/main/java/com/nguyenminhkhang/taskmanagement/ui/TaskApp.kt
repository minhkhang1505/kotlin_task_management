package com.nguyenminhkhang.taskmanagement.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import com.nguyenminhkhang.taskmanagement.ui.theme.TaskManagementTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nguyenminhkhang.taskmanagement.ui.common.navigationbar.Destination
import com.nguyenminhkhang.taskmanagement.ui.common.navigationbar.NavigationBottomBar
import com.nguyenminhkhang.taskmanagement.ui.home.HomeScreenRoute
import com.nguyenminhkhang.taskmanagement.ui.register.RegisterPage
import com.nguyenminhkhang.taskmanagement.ui.register.state.RegisterState
import com.nguyenminhkhang.taskmanagement.ui.repeat.RepeatPage
import com.nguyenminhkhang.taskmanagement.ui.signin.SignInPage
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.TaskDetailPage

@Composable
fun TaskApp() {
    TaskManagementTheme {
        val navController = rememberNavController()
        val startDestination = Destination.Home
        val selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        NavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            startDestination = NavScreen.LOGIN.route
        ) {
            composable(route = NavScreen.LOGIN.route) {
                SignInPage(navController = navController)
            }

            composable(route = NavScreen.REGISTER.route) {
                RegisterPage(registerState = RegisterState(), navController = navController)
            }

            composable(route = NavScreen.HOME.route) { backStackEntry ->
                HomeScreenRoute(navController = navController, backStackEntry = backStackEntry,
                    selectedDestination = selectedDestination, currentRoute = currentRoute)
            }

            composable(
                route = NavScreen.TASK_DETAIL.route,
                arguments = listOf(navArgument("taskId") { type = NavType.LongType })
            ) {
                TaskDetailPage(
                    navController = navController,
                )
            }

            composable(
                route = NavScreen.REPEAT.route,
                arguments = listOf(navArgument("taskId") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("taskId")
                RepeatPage(navController = navController)
            }
        }
    }
}

enum class NavScreen(val route: String) {
    LOGIN("Login"),
    REGISTER("Register"),
    HOME("Home"),
    TASK_DETAIL("TaskDetail/{taskId}"),
    REPEAT("Repeat/{taskId}"),
}