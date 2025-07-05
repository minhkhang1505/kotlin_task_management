package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.datepicker.convertMillisToDate
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTaskUiState
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskDetailScreenUiState(
    val task: TaskUiState? = null, // Data can be null initially
    val repeatSummaryText: String = "",
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true,
    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val isInEditMode: Boolean = false,
)


@HiltViewModel
class TaskDetailViewModel @Inject constructor (
    private val taskRepo : TaskRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailScreenUiState())
    val uiState: StateFlow<TaskDetailScreenUiState> = _uiState.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<SnackbarEvent>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()

    private val taskId: Long = savedStateHandle.get<Long>("taskId")!!

    init {
        viewModelScope.launch {
            taskRepo.getTaskById(taskId).collect { taskEntity ->
                _uiState.update { currentState ->
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

    fun onFavoriteClick() {

    }

    fun onFavoriteChange() {

    }

    // Update task title
    fun onTitleChange(newContent: String) {
        _uiState.update { currentState ->
            currentState.copy(task = currentState.task?.copy(content = newContent))
        }
    }

    fun saveTitle() {
        viewModelScope.launch {
            val currentTitle = _uiState.value.task?.content ?: return@launch
            taskRepo.updateTaskContentById(taskId, currentTitle)
            _snackbarEvent.emit(SnackbarEvent("Task updated"))
        }
    }

    fun onEnterEditMode() {
        _uiState.update { it.copy(isInEditMode = true) }
    }

    fun onExitEditMode() {
        saveTitle()
        _uiState.update { it.copy(isInEditMode = false) }
    }

    // Update task detail
    fun onDetailChange(contentDetail: String) {
        _uiState.update { currentState ->
            currentState.copy(task = currentState.task?.copy(taskDetail = contentDetail))
        }
    }

    fun saveDetail() {
        viewModelScope.launch {
            val currentDetail = _uiState.value.task?.taskDetail ?: return@launch
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
        _uiState.update { it.copy(isDatePickerVisible = true) }
    }

    fun onDismissDatePicker() {
        _uiState.update { it.copy(isDatePickerVisible = false) }
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
        _uiState.update { it.copy(isTimePickerVisible = true) }
    }

    fun onDismissTimePicker() {
        _uiState.update {
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