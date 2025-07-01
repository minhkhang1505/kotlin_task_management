package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    savedStateHandle: SavedStateHandle
): ViewModel(), TaskDetailDelegate {

    private val _taskDetail = MutableStateFlow<TaskUiState>(TaskUiState(
        id = null,
        content = "",
        isFavorite = false,
        isCompleted = false,
        collectionId = 0L,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        stringUpdateAt = System.currentTimeMillis().millisToDateString(),
        repeatEvery = 1L,
        repeatDaysOfWeek = null,
        repeatInterval = null,
        repeatEndType = null,
        repeatEndDate = null,
        startDate = null,
        repeatEndCount = 1,
        startTime = null
    ))

    private val _snackbarEvent = MutableSharedFlow<SnackbarEvent>()

    var task = _taskDetail.asStateFlow()

    init {
        val taskId: Long? = savedStateHandle.get("taskId")
        Log.d("TaskDetailViewModel", "Received taskId: $taskId")

        if (taskId != null) {
            viewModelScope.launch {
                val taskEntity = taskRepo.getTaskById(taskId)
                _taskDetail.value = taskEntity.toTaskUiState()
                Log.d("TaskDetailViewModel", "Task detail loaded for taskId: ${_taskDetail.value}")
            }

        } else {
            // Xử lý trường hợp lỗi không nhận được ID
        }
    }

    override fun updateTaskContentById(newContent:String) {
        viewModelScope.launch {
            val taskId = _taskDetail.value.id ?: return@launch
            val result = taskRepo.updateTaskContentById(taskId, newContent)
            if(result) {
                _taskDetail.value = _taskDetail.value.copy (content = newContent)
                _snackbarEvent.emit(SnackbarEvent("Task content updated successfully"))
            } else {
                _snackbarEvent.emit(SnackbarEvent("Failed to update task content"))
            }
        }
    }
}

interface TaskDetailDelegate {
    fun updateTaskContentById(newContent: String)
}
