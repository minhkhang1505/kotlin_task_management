package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor (
    private val taskRepo : TaskRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _task = MutableStateFlow<TaskEntity>(
        TaskEntity(
            id = -2000,
            content = "",
            isFavorite = false,
            isCompleted = false,
            collectionId = 0,
            updatedAt = 0L
        )
    )
    var task = _task.asStateFlow()

    init {
        val taskId: Long? = savedStateHandle.get("taskId")
        Log.d("TaskDetailViewModel", "Received taskId: $taskId")

        if (taskId != null) {
            viewModelScope.launch {
                val taskEntity = taskRepo.getTaskById(taskId)
                _task.value = taskEntity
            }
        } else {
            // Xử lý trường hợp lỗi không nhận được ID
        }
    }
}
