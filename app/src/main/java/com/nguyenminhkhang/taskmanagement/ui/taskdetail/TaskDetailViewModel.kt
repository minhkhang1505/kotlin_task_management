package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.notice.TaskScheduler
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.datepicker.convertMillisToDate
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTaskUiState
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarEvent
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.state.TaskDetailScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor (
    private val taskRepo : TaskRepo,
    private val scheduler: TaskScheduler,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _taskUiState = MutableStateFlow(TaskDetailScreenUiState())
    val uiState: StateFlow<TaskDetailScreenUiState> = _taskUiState.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<SnackbarEvent>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val taskId: Long = savedStateHandle.get<Long>("taskId")!!

    init {
        viewModelScope.launch {
            taskRepo.getTaskById(taskId).collect { taskEntity ->
                _taskUiState.update { currentState ->
                    val taskUiState = taskEntity.toTaskUiState()
                    currentState.copy(
                        task = taskEntity.toTaskUiState(),
                        isLoading = false,
                        repeatSummaryText = buildRepeatSummaryText(taskUiState)
                    )
                }
            }
        }
    }

    fun onMarkAsDoneClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateBackWithResult(taskId))
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentTask = _taskUiState.value.task ?: return@launch
            val isFavorite = !currentTask.isFavorite
            taskRepo.updateTaskFavoriteById(taskId, isFavorite)
            _snackbarEvent.emit(SnackbarEvent("Task ${if (isFavorite) "added to" else "removed from"} favorites"))
        }
    }

    // Update task title
    fun onTitleChange(newContent: String) {
        _taskUiState.update { currentState ->
            currentState.copy(task = currentState.task?.copy(content = newContent))
        }
    }

    fun saveTitle() {
        viewModelScope.launch {
            val currentTitle = _taskUiState.value.task?.content ?: return@launch
            taskRepo.updateTaskContentById(taskId, currentTitle)
            _snackbarEvent.emit(SnackbarEvent("Task updated"))
        }
    }

    fun onEnterEditMode() {
        _taskUiState.update { it.copy(isInEditMode = true) }
    }

    fun onExitEditMode() {
        saveTitle()
        _taskUiState.update { it.copy(isInEditMode = false) }
    }

    // Update task detail
    fun onDetailChange(contentDetail: String) {
        _taskUiState.update { currentState ->
            currentState.copy(task = currentState.task?.copy(taskDetail = contentDetail))
        }
    }

    fun saveDetail() {
        viewModelScope.launch {
            val currentDetail = _taskUiState.value.task?.taskDetail ?: return@launch
            taskRepo.updateTaskDetailById(taskId, currentDetail)
            _snackbarEvent.emit(SnackbarEvent("Task detail updated"))
        }
    }

    //For date picker
    fun onDateSelected(dateInMillis: Long) {
        viewModelScope.launch {
            taskRepo.updateTaskStartDate(taskId, dateInMillis)
            onDismissDatePicker()
        }
    }

    fun onClearDateSelected() {
        viewModelScope.launch {
            taskRepo.clearDateSelected(taskId)
        }
    }

    fun onShowDatePicker() {
        _taskUiState.update { it.copy(isDatePickerVisible = true) }
    }

    fun onDismissDatePicker() {
        _taskUiState.update { it.copy(isDatePickerVisible = false) }
    }

    // For time picker
    fun onTimeSelected(timeSelected: Long) {
        viewModelScope.launch {
            taskRepo.updateTaskStartTime(taskId, timeSelected)
            onDismissTimePicker()
        }
    }

    fun onClearTimeSelected() {
        viewModelScope.launch {
            taskRepo.clearTimeSelected(taskId)
        }
    }

    fun onShowTimePicker() {
        _taskUiState.update { it.copy(isTimePickerVisible = true) }
    }

    fun onDismissTimePicker() {
        _taskUiState.update {
            it.copy(isTimePickerVisible = false)
        }
    }

    // For repeat summary text
    private fun buildRepeatSummaryText(task: TaskUiState?): String {
        if (task == null || (task.repeatInterval == null && task.repeatDaysOfWeek.isNullOrEmpty())) {
            return ""
        }

        val repeatContent = StringBuilder()

        if (task.repeatInterval != null) {
            repeatContent.append("Once every ${task.repeatEvery} ${task.repeatInterval.lowercase()}")
        }
        if (!task.repeatDaysOfWeek.isNullOrEmpty()) {
            repeatContent.append(" on ${task.repeatDaysOfWeek.joinToString(", ")}")
        }
        if (task.repeatEndDate != null && task.repeatEndType == "At") {
            repeatContent.append(", until ${convertMillisToDate(task.repeatEndDate)}")
        }
        if (task.repeatEndType == "After") {
            repeatContent.append(", ${task.repeatEndCount} occurrences")
        }

        return repeatContent.toString()
    }
}

sealed class NavigationEvent {
    data class NavigateBackWithResult(val taskId: Long) : NavigationEvent()
}