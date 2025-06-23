package com.nguyenminhkhang.taskmanagement.ui.home

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.MainEvent
import com.nguyenminhkhang.taskmanagement.MainViewModel
import com.nguyenminhkhang.taskmanagement.ui.AppMenuItem
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarActionType
import kotlinx.coroutines.launch

@Composable
fun HomeScreenRoute(
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavController,
    backStackEntry: NavBackStackEntry
) {
    val listTabGroup by mainViewModel.listTabGroup.collectAsStateWithLifecycle(emptyList())
    var isShowAddNewCollectionButton by remember { mutableStateOf(false) }
    var menuListButtonSheet by remember{mutableStateOf<List<AppMenuItem>?>(null) }
    Log.d("INSTANCE_CHECK", "ViewModel đang LẮNG NGHE SNACKBAR có HashCode: ${mainViewModel.hashCode()}")
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true) {

        launch {
            backStackEntry.savedStateHandle
                .getStateFlow<Long?>("task_completed_id", null)
                .collect { taskId ->
                    if (taskId != null) {
                        Log.d("DEBUG_FLOW", "2. NHẬN KẾT QUẢ: Collector đã chạy, nhận được ID = $taskId")
                        backStackEntry.savedStateHandle.remove<Long>("task_completed_id")
                        mainViewModel.handleTaskCompletionResult(taskId)
                    }
                }
        }

        launch {
            mainViewModel.snackBarEvent.collect { event ->
                Log.d("DEBUG_FLOW", "6. NHẬN LỆNH SNACKBAR: Collector đã nhận được sự kiện: ${event.message}")
                val result = snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.actionLabel,
                    duration = event.duration
                )
                if (result == SnackbarResult.ActionPerformed) {
                    if (event.actionType == SnackbarActionType.UNDO_TOGGLE_COMPLETE) {
                        mainViewModel.undoToggleComplete()
                    }
                } else if (result == SnackbarResult.Dismissed) {
                    if (event.actionType == SnackbarActionType.UNDO_TOGGLE_COMPLETE) {
                        mainViewModel.confirmToggleComplete()
                    }
                }
            }
        }

        launch {
            mainViewModel.eventFlow.collect { event ->
                when (event) {
                    MainEvent.RequestAddNewCollection -> {
                        isShowAddNewCollectionButton = true
                    }
                    is MainEvent.RequestShowButtonSheetOption -> {
                        menuListButtonSheet = event.list
                    }
                }
            }
        }
    }

    HomeLayout(
        navController = navController,
        snackbarHostState = snackbarHostState,
        listTabGroup = listTabGroup,
        taskDelegate = mainViewModel,
    )
}