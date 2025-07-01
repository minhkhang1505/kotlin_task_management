package com.nguyenminhkhang.taskmanagement.ui.repeat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.millisToDateString
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepeatViewModel @Inject constructor(
    private  val taskRepo: TaskRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel(), RepeatDelegate {

    /**
     * Update the repeat settings of a task by its ID.
     *
     * @param taskId The ID of the task to update.
     * @param repeatEvery The frequency of the repeat (e.g., daily, weekly).
     * @param repeatDaysOfWeek The days of the week on which the task should repeat.
     * @param repeatInterval The interval for the repeat (e.g., every 2 weeks).
     * @param repeatEndType The type of end condition for the repeat (e.g., after a certain date or count).
     * @param repeatEndDate The date when the repeat should end, if applicable.
     * @param repeatEndCount The number of times the task should repeat, if applicable.
     */

    private val _taskDetail = MutableStateFlow<TaskUiState>(
        TaskUiState(
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
    init {
        val taskId: Long? = savedStateHandle.get("taskId")

        if (taskId != null) {
            viewModelScope.launch {
                val taskEntity  = taskRepo.getTaskById(taskId)
                _taskDetail.value = taskEntity.toTaskUiState()
                Log.d("TaskDetailViewModel", "Task loaded: ${_taskDetail.value.repeatEvery}")
            }
        } else {
            Log.e("TaskDetailViewModel", "Task ID is null")
        }
    }

    var task = _taskDetail.asStateFlow()

    override fun updateTaskRepeatById(
        taskId: Long,
        repeatEvery: Long,
        repeatDaysOfWeek: String?,
        repeatInterval: String?,
        repeatEndType: String?,
        repeatEndDate: Long?,
        repeatEndCount: Int,
        startTime: Long?
    ) {
        viewModelScope.launch {
            taskRepo.updateTaskRepeatById(
                taskId = taskId,
                repeatEvery = repeatEvery,
                repeatDaysOfWeek = repeatDaysOfWeek,
                repeatInterval = repeatInterval,
                repeatEndType = repeatEndType,
                repeatEndDate = repeatEndDate,
                repeatEndCount = repeatEndCount,
                startTime = startTime
            )
        }
    }
}

interface RepeatDelegate {
    fun updateTaskRepeatById(
        taskId: Long,
        repeatEvery: Long,
        repeatDaysOfWeek: String?,
        repeatInterval: String?,
        repeatEndType: String?,
        repeatEndDate: Long?,
        repeatEndCount: Int,
        startTime: Long?
    )
}