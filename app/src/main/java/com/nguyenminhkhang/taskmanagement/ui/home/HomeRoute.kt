package com.nguyenminhkhang.taskmanagement.ui.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.nguyenminhkhang.taskmanagement.ui.home.action.buildActionMenuItem
import com.nguyenminhkhang.taskmanagement.ui.home.event.TaskEvent
import com.nguyenminhkhang.taskmanagement.ui.home.sort.buildSortMenuItems
import com.nguyenminhkhang.taskmanagement.ui.common.snackbar.SnackbarActionType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = koinViewModel(),
    onNavigateToTaskDetail: (Long) -> Unit,
    backStackEntry: NavBackStackEntry,
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val strings = homeViewModel.stringProvider

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
                snackBarHostState.currentSnackbarData?.dismiss()
                val result = snackBarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.actionLabel,
                    duration = event.duration
                )

                if(result == SnackbarResult.ActionPerformed) {
                    when (event.actionType) {
                        SnackbarActionType.UNDO_TOGGLE_COMPLETE -> {
                            homeViewModel.onEvent(TaskEvent.UndoToggleComplete)
                        }
                        null -> {}
                    }
                }
            }
        }
    }

    val sortMenuItems = buildSortMenuItems(
        strings = strings,
        collectionId = uiState.currentCollectionId,
        onEvent = homeViewModel::onEvent
    )

    val actionMenuItems = buildActionMenuItem(
        strings = strings,
        collectionId = uiState.currentCollectionId,
        onEvent = homeViewModel::onEvent
    )

    HomeScreen(
        uiState = uiState,
        sortMenuItems = sortMenuItems,
        actionMenuItems = actionMenuItems,
        onEvent = homeViewModel::onEvent,
        snackBarHostState = snackBarHostState,
        onNavigateToTaskDetail = onNavigateToTaskDetail,
        onScreenShown = homeViewModel::onScreenShown
    )
}