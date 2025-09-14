package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.datepicker.convertMillisToDate
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarEvent
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.state.TaskDetailScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor (
    private val taskRepo : TaskRepo,
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
        val taskFlow = taskRepo.getTaskById(taskId)
        val collectionsFlow = taskRepo.getTaskCollection()

        viewModelScope.launch {
            combine(taskFlow, collectionsFlow) { taskEntity, collections ->
                if (taskEntity == null) {
                    TaskDetailScreenUiState(isLoading = false, task = null, collection = collections)
                } else {
                    val taskUiState = taskEntity
                    val currentCollectionName = collections.find { it.id == taskUiState.collectionId }?.content ?: ""

                    TaskDetailScreenUiState(
                        task = taskUiState,
                        collection = collections,
                        currentCollection = currentCollectionName,
                        repeatSummaryText = buildRepeatSummaryText(taskUiState),
                        isLoading = false
                    )
                }
            }.collect { combinedUiState ->
                _taskUiState.value = combinedUiState
            }
        }
    }

    fun onMarkAsDoneClicked() {
        viewModelScope.launch {
            taskRepo.updateTaskCompleted(taskId, true)
            _navigationEvent.emit(NavigationEvent.NavigateBackWithResult(taskId))
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentTask = _taskUiState.value.task ?: return@launch
            val isFavorite = !currentTask.favorite
            taskRepo.updateTaskFavorite(taskId, isFavorite)
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
            val currentTask = _taskUiState.value.task ?: return@launch
            taskRepo.updateTask(currentTask)
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
            val currentDetail = _taskUiState.value.task ?: return@launch
            taskRepo.updateTask(currentDetail)
            _snackbarEvent.emit(SnackbarEvent("Task detail updated"))
        }
    }

    //For date picker
    fun onDateSelected(dateInMillis: Long) {
        _taskUiState.update { currentTask ->
            currentTask.copy( task = currentTask.task?.copy(startDate = dateInMillis), isDatePickerVisible = false)
        }

        val currentTask = _taskUiState.value.task ?: return

        viewModelScope.launch {
            taskRepo.updateTask(currentTask)
        }
    }

    fun onClearDateSelected() {
        _taskUiState.update { currentTask ->
            currentTask.copy(task = currentTask.task?.copy(startDate = null))
        }

        val currentTask = _taskUiState.value.task ?: return

        viewModelScope.launch {
            taskRepo.updateTask(currentTask)
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
        _taskUiState.update { currentTask ->
            currentTask.copy( task = currentTask.task?.copy(startTime = timeSelected), isTimePickerVisible = false)
        }
        val currentTask = _taskUiState.value.task ?: return
        viewModelScope.launch {
            taskRepo.updateTask(currentTask)
        }
    }

    fun onClearTimeSelected() {
        _taskUiState.update { currentTask ->
            currentTask.copy(task = currentTask.task?.copy(startTime = null))
        }

        val currentTask = _taskUiState.value.task ?: return

        viewModelScope.launch {
            taskRepo.updateTask(currentTask)
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

    fun addTaskToCalendar(context: Context, task: TaskEntity) {
        val startTimeMillis = uiState.value.task?.reminderTimeMillis

        if(startTimeMillis == null) {
            Toast.makeText(context, "Please set a reminder time for the task", Toast.LENGTH_SHORT).show()
            return
        }

        val endTimeMillis = (uiState.value.task?.reminderTimeMillis ?: 0L) + 3600 * 1000

        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI

            putExtra(CalendarContract.Events.TITLE, task.content)
            putExtra(CalendarContract.Events.DESCRIPTION, task.taskDetail)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimeMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTimeMillis)
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
            Toast.makeText(context, "Task added to calendar", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No calendar app found", Toast.LENGTH_SHORT).show()
        }
    }

    // For repeat summary text
    private fun buildRepeatSummaryText(task: TaskEntity?): String {
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

    fun onEvent(event: TaskDetailEvent) {
        when(event) {
            is TaskDetailEvent.AddToCalendar -> addTaskToCalendar(event.context, event.task)
            is TaskDetailEvent.GetCurrentCollectionNameById -> {
                _taskUiState.update { currentState ->
                    currentState.copy(
                        currentCollection = currentState.collection.find { it.id == event.collectionId }?.content ?: ""
                    )
                }
            }
            is TaskDetailEvent.ShowChangeCollectionSheet -> {
                _taskUiState.update { currentState ->
                    currentState.copy(isChangeCollectionSheetVisible = true)
                }
            }
            is TaskDetailEvent.CloseChangeCollectionSheet -> {
                _taskUiState.update { currentState ->
                    currentState.copy(isChangeCollectionSheetVisible = false)
                }
            }
            is TaskDetailEvent.CurrentCollectionChanged -> {
                viewModelScope.launch {
                    taskRepo.updateTaskCollectionById(taskId, event.collectionId)
                    _taskUiState.update { currentState ->
                        currentState.copy(
                            currentCollection = currentState.collection.find { it.id == event.collectionId }?.content ?: ""
                        )
                    }
                }
            }

            is TaskDetailEvent.ToggleFavorite -> toggleFavorite()
            is TaskDetailEvent.OnTitleChanged -> onTitleChange(event.newTitle)
            is TaskDetailEvent.SaveTitle -> saveTitle()
            is TaskDetailEvent.OnEnterEditMode -> onEnterEditMode()
            is TaskDetailEvent.OnExitEditMode -> onExitEditMode()
            is TaskDetailEvent.OnDetailChange -> onDetailChange(event.contentDetail)
            is TaskDetailEvent.SaveDetail -> saveDetail()
            is TaskDetailEvent.OnShowDatePicker -> onShowDatePicker()
            is TaskDetailEvent.OnDismissDatePicker -> onDismissDatePicker()
            is TaskDetailEvent.OnClearDateSelected -> onClearDateSelected()
            is TaskDetailEvent.OnDateSelected -> onDateSelected(event.dataMillis)
            is TaskDetailEvent.OnShowTimePicker -> onShowTimePicker()
            is TaskDetailEvent.OnClearTimeSelected -> onClearTimeSelected()
            is TaskDetailEvent.OnTimeSelected -> onTimeSelected(event.timeMillis)
            is TaskDetailEvent.OnDismissTimePicker -> onDismissTimePicker()
            is TaskDetailEvent.OnMarkAsDoneClicked -> onMarkAsDoneClicked()
        }
    }
}

sealed class NavigationEvent {
    data class NavigateBackWithResult(val taskId: Long) : NavigationEvent()
}