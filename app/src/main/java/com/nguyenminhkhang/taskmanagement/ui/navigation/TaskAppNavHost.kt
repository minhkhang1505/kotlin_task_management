package com.nguyenminhkhang.taskmanagement.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nguyenminhkhang.taskmanagement.ui.auth.register.RegisterRoute
import com.nguyenminhkhang.taskmanagement.ui.auth.signin.SignInRoute
import com.nguyenminhkhang.taskmanagement.ui.common.navigationbar.NavigationBottomBar
import com.nguyenminhkhang.taskmanagement.ui.home.HomeRoute
import com.nguyenminhkhang.taskmanagement.ui.repeat.RepeatRoute
import com.nguyenminhkhang.taskmanagement.ui.search.SearchRoute
import com.nguyenminhkhang.taskmanagement.ui.settings.account.AccountRoute
import com.nguyenminhkhang.taskmanagement.ui.settings.appearance.LanguageRoute
import com.nguyenminhkhang.taskmanagement.ui.settings.appearance.ThemeRoute
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.TaskDetailRoute
import timber.log.Timber
import kotlin.let

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskAppNavHost(
    navController: NavHostController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val showBottomBar = backStackEntry?.destination?.let { dest ->
        dest.hasRoute<HomeRoute>() ||
        dest.hasRoute<SearchRoute>() ||
        dest.hasRoute<AccountRoute>()
    } == true

    LaunchedEffect(Unit) {
        Timber.d("Current destination: ${navController.currentDestination}")
    }

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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            startDestination = SignInRoute
        ) {

            composable<SignInRoute> {
                SignInRoute(onNavigateToHome = {
                    Timber.d("Before navigate to Home Route")
                    navController.navigate(HomeRoute)
                    Timber.d("Navigate to Home Route success")
                })
            }

            composable<RegisterRoute> {
                RegisterRoute(onNavigateToSignIn = { navController.navigate(SignInRoute) })
            }

            composable<AccountRoute> {
                AccountRoute(onNavigateToLanguage = { navController.navigate(LanguageRoute)},
                    onNavigateToTheme = { navController.navigate(ThemeRoute)},)
            }

            composable<SearchRoute> {
                SearchRoute(
                    onNavigateToTaskDetail = {taskId ->
                        navController.navigate(TaskDetailRoute(taskId))
                    }
                )
            }

            composable<ThemeRoute> {
                ThemeRoute(onPopBackStack = { navController.popBackStack() })
            }

            composable<LanguageRoute> {
                LanguageRoute( onPopBackStack = { navController.popBackStack() }
                )
            }

            composable<HomeRoute> { backStackEntry ->
                HomeRoute(
                    onNavigateToTaskDetail = { taskId ->
                        Timber.d("Before navigate to TaskDetail with taskId: ${taskId}")
                        navController.navigate(TaskDetailRoute(taskId))
                    }, backStackEntry = backStackEntry
                )
            }

            composable<TaskDetailRoute>
            {
                TaskDetailRoute(
                    onNavigateToRepeat = { taskId ->
                        Timber.d("Nav to TaskDetailRoute with taskId: ${taskId}")
                        navController.navigate(RepeatRoute(taskId))
                        Timber.d("Navigate to TaskDetailRoute success")
                    },
                    onPopBackStack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<RepeatRoute>{
                RepeatRoute(
                    onPopBackStack = { navController.popBackStack() }
                )
            }
        }
    }
}