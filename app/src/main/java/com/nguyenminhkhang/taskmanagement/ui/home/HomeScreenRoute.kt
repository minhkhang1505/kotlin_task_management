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

        // Coroutine con #1: Lắng nghe kết quả trả về từ màn hình khác
        launch {
            backStackEntry.savedStateHandle
                .getStateFlow<Long?>("task_completed_id", null)
                .collect { taskId ->
                    // Chỉ xử lý khi nhận được ID hợp lệ
                    if (taskId != null) {
                        Log.d("DEBUG_FLOW", "2. NHẬN KẾT QUẢ: Collector đã chạy, nhận được ID = $taskId")

                        // ✅ BƯỚC 1: XÓA KẾT QUẢ NGAY LẬP TỨC ĐỂ "TIÊU THỤ" NÓ
                        //    Điều này ngăn coroutine này bị kích hoạt lại một cách không cần thiết.
                        backStackEntry.savedStateHandle.remove<Long>("task_completed_id")

                        // ✅ BƯỚC 2: BÂY GIỜ MỚI GỌI VIEWMODEL ĐỂ XỬ LÝ
                        //    Coroutine sẽ có đủ thời gian để chạy mà không bị hủy giữa chừng.
                        mainViewModel.handleTaskCompletionResult(taskId)
                    }
                }
        }

        // Coroutine con #2: Lắng nghe sự kiện Snackbar
        launch {
            mainViewModel.snackBarEvent.collect { event ->
                Log.d("DEBUG_FLOW", "6. NHẬN LỆNH SNACKBAR: Collector đã nhận được sự kiện: ${event.message}")
                val result = snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.actionLabel,
                    duration = event.duration
                )
                // Xử lý kết quả từ Snackbar
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

        // Coroutine con #3: Lắng nghe các sự kiện UI khác
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