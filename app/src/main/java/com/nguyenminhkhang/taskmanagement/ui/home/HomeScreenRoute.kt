package com.nguyenminhkhang.taskmanagement.ui.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarActionType
import kotlinx.coroutines.launch

@Composable
fun HomeScreenRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    selectedDestination: Int,
    currentRoute: String? = null
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val searchState by homeViewModel.searchState.collectAsState()
    val results by homeViewModel.searchResults.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        launch {
            backStackEntry.savedStateHandle
                .getStateFlow<Long?>("task_completed_id", null)
                .collect { taskId ->
                    if (taskId != null) {
                        backStackEntry.savedStateHandle.remove<Long>("task_completed_id")
                    }
                }
        }

        launch {
            homeViewModel.snackBarEvent.collect { event ->
                snackbarHostState.currentSnackbarData?.dismiss()
                val result = snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.actionLabel,
                    duration = event.duration
                )

                if(result == SnackbarResult.ActionPerformed) {
                    when (event.actionType) {
                        SnackbarActionType.UNDO_TOGGLE_COMPLETE -> {
                            homeViewModel.onEvent(HomeEvent.UndoToggleComplete)
                        }
                        null -> {}
                    }
                }
            }
        }
    }

    HomeLayout(
        currentRoute = currentRoute,
        selectedDestination = selectedDestination,
        searchResults = results,
        searchState = searchState,
        uiState = uiState,
        onEvent = homeViewModel::onEvent,
        snackbarHostState = snackbarHostState,
        navController = navController
    )
}