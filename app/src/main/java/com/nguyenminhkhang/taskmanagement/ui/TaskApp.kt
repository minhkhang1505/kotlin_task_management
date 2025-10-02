package com.nguyenminhkhang.taskmanagement.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import com.nguyenminhkhang.taskmanagement.ui.common.theme.TaskManagementTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nguyenminhkhang.taskmanagement.ui.settings.account.AccountPage
import com.nguyenminhkhang.taskmanagement.ui.settings.account.AccountViewModel
import com.nguyenminhkhang.taskmanagement.ui.settings.appearance.LanguagePage
import com.nguyenminhkhang.taskmanagement.ui.settings.appearance.ThemeModePage
import com.nguyenminhkhang.taskmanagement.ui.common.navigationbar.NavigationBottomBar
import com.nguyenminhkhang.taskmanagement.ui.home.HomePage
import com.nguyenminhkhang.taskmanagement.ui.auth.register.RegisterPage
import com.nguyenminhkhang.taskmanagement.ui.repeat.RepeatPage
import com.nguyenminhkhang.taskmanagement.ui.search.SearchPage
import com.nguyenminhkhang.taskmanagement.ui.auth.signin.SignInPage
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.TaskDetailPage

@Composable
fun TaskApp() {
    val accountViewModel = hiltViewModel<AccountViewModel>()

    TaskManagementTheme(viewModel = accountViewModel) {
        val navController = rememberNavController()
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        val showBottomBar = currentRoute in listOf(
            NavScreen.HOME.route,
            NavScreen.SEARCH.route,
            NavScreen.ACCOUNT.route
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (showBottomBar) {
                    NavigationBottomBar(navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                startDestination = NavScreen.LOGIN.route
            ) {
                composable(route = NavScreen.LOGIN.route) {
                    SignInPage(navController = navController)
                }

                composable(route = NavScreen.REGISTER.route) {
                    RegisterPage(navController = navController)
                }

                composable(route = NavScreen.HOME.route) { backStackEntry ->
                    HomePage(
                        navController = navController, backStackEntry = backStackEntry
                    )
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
                ) {
                    RepeatPage(navController = navController)
                }

                composable(route = NavScreen.ACCOUNT.route) {
                    AccountPage(
                        navController = navController
                    )
                }

                composable(route = NavScreen.SEARCH.route) {
                    SearchPage(
                        navController = navController
                    )
                }

                composable(route = NavScreen.THEME_MODE.route) {
                    ThemeModePage(
                        navController = navController
                    )
                }

                composable(route = NavScreen.LANGUAGE.route) {
                    LanguagePage(
                        navController = navController
                    )
                }
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
    ACCOUNT("Account"),
    SEARCH("Search"),
    THEME_MODE("ThemeMode"),
    LANGUAGE("Language")
}