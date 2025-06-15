package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.handler.TaskCompletionHandler
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.millisToDateString
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTaskUiState
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor (
    private val taskRepo : TaskRepo,
    private val completionHandler: TaskCompletionHandler,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _taskDetail = MutableStateFlow<TaskUiState>(TaskUiState(
        id = null,
        content = "",
        isFavorite = false,
        isCompleted = false,
        collectionId = 0L,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        stringUpdateAt = System.currentTimeMillis().millisToDateString()
    ))
    val taskDetail = _taskDetail.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<SnackbarEvent>()
    val snackBarEvent = _snackbarEvent.asSharedFlow()

    var task = _taskDetail.asStateFlow()

    init {
        val taskId: Long? = savedStateHandle.get("taskId")
        Log.d("TaskDetailViewModel", "Received taskId: $taskId")

        if (taskId != null) {
            viewModelScope.launch {
                val taskEntity = taskRepo.getTaskById(taskId)
                _taskDetail.value = taskEntity.toTaskUiState()
            }
        } else {
            // Xử lý trường hợp lỗi không nhận được ID
        }
    }

    fun invertTaskCompleted(taskUiState: TaskUiState) {
        completionHandler.invertTaskCompletion(
            scope = viewModelScope,
            task = taskUiState,
            onUpdateList = { updatedTask ->
                _taskDetail.value = updatedTask
            },
            onShowSnackbar = { event ->
                _snackbarEvent.emit(event)
            }
        )
    }

    fun undoToggleComplete() {
        completionHandler.undo { originalTask ->
            _taskDetail.value = originalTask
        }
    }

    fun confirmToggleComplete() {
        completionHandler.confirm(viewModelScope)
    }
}
