package com.nguyenminhkhang.taskmanagement.handler

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.nguyenminhkhang.taskmanagement.database.domain.ToggleCompleteUseCase
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarActionType
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarEvent
import com.nguyenminhkhang.taskmanagement.worker.UpdateTaskWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskCompletionHandler @Inject constructor(
    private val toggleCompleteUseCase: ToggleCompleteUseCase
) {
    private var lastToggledCompleteTask: TaskUiState? = null

    // Hàm này sẽ chứa toàn bộ logic, nhận vào scope, task và 2 lambda để cập nhật
    fun invertTaskCompletion(
        scope: CoroutineScope,
        task: TaskUiState,
        onUpdateList: (TaskUiState) -> Unit, // Lambda để ViewModel cập nhật UI
        onShowSnackbar: suspend (SnackbarEvent) -> Unit // Lambda để ViewModel hiện Snackbar
    ) {
        lastToggledCompleteTask = task
        val toggledTask =
            task.copy(isCompleted = !task.isCompleted, updatedAt = System.currentTimeMillis())

        onUpdateList(toggledTask)

        val message =
            "Marked as ${if (toggledTask.isCompleted) "completed" else "uncompleted"} '${toggledTask.content}'"
        scope.launch {
            onShowSnackbar(
                SnackbarEvent(
                    message = message,
                    duration = SnackbarDuration.Long,
                    actionLabel = "Undo",
                    actionType = SnackbarActionType.UNDO_TOGGLE_COMPLETE
                )
            )
        }
    }

    // Khi người dùng nhấn Hoàn tác
    fun undo(onUpdateList: (TaskUiState) -> Unit) {
        lastToggledCompleteTask?.let { originalTask ->
            onUpdateList(originalTask)
            lastToggledCompleteTask = null
        }
    }

    // Khi Snackbar hết hạn
    fun confirm(scope: CoroutineScope) {
        lastToggledCompleteTask?.let { originalTask ->
            scope.launch(Dispatchers.IO) {
                try {
                    val result = toggleCompleteUseCase(originalTask.id!!, !originalTask.isCompleted)
                    Log.d("TaskCompletionHandler", "Database update result: $result")
                } catch (e: Exception) {
                    Log.e("TaskCompletionHandler", "Error updating task completion: ${e.message}")
                }
            }
            lastToggledCompleteTask = null
        }
    }
}