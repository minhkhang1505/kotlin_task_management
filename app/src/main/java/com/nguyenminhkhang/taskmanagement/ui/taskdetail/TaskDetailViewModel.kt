package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.domain.usecase.GetTaskByIdUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.ToggleCompleteUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.ToggleTaskFavoriteUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.UpdateTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.GetTaskCollectionsUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.MoveTaskToCollectionUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.taskdetail.BuildRepeatSummaryTextUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.taskdetail.TrackTaskDetailScreenViewUseCase
import com.nguyenminhkhang.taskmanagement.ui.common.snackbar.SnackbarEvent
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.effects.TaskDetailEffect
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.state.TaskDetailScreenUiState
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.events.NavigationEvent
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.events.TaskDetailEvent
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
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val getTaskCollectionsUseCase: GetTaskCollectionsUseCase,
    private val toggleCompleteUseCase: ToggleCompleteUseCase,
    private val toggleTaskFavoriteUseCase: ToggleTaskFavoriteUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val moveTaskToCollectionUseCase: MoveTaskToCollectionUseCase,
    private val buildRepeatSummaryTextUseCase: BuildRepeatSummaryTextUseCase,
    private val trackTaskDetailScreenViewUseCase: TrackTaskDetailScreenViewUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _taskUiState = MutableStateFlow(TaskDetailScreenUiState())
    val uiState: StateFlow<TaskDetailScreenUiState> = _taskUiState.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<SnackbarEvent>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _effect = MutableSharedFlow<TaskDetailEffect>()
    val effect = _effect.asSharedFlow()

    private val taskId: Long? = savedStateHandle.get<Long>("taskId")

    init {
        val safeTaskId = taskId
        if (safeTaskId == null) {
            _taskUiState.update { it.copy(isLoading = false) }
            viewModelScope.launch {
                _snackbarEvent.emit(SnackbarEvent("Unable to load task details"))
            }
        } else {
            val taskFlow = getTaskByIdUseCase(safeTaskId)
            val collectionsFlow = getTaskCollectionsUseCase()

            viewModelScope.launch {
                combine(taskFlow, collectionsFlow) { taskUiState, collections ->
                        val currentCollectionName = collections.find { it.id == taskUiState.collectionId }?.content ?: ""

                        TaskDetailScreenUiState(
                            task = taskUiState,
                            collection = collections,
                            currentCollection = currentCollectionName,
                            repeatSummaryText = buildRepeatSummaryTextUseCase(taskUiState),
                            isLoading = false
                        )
                }.collect { combinedUiState ->
                    _taskUiState.value = combinedUiState
                }
            }
        }
    }

    fun onScreenShow() {
        trackTaskDetailScreenViewUseCase()
    }

    fun onMarkAsDoneClicked() {
        val safeTaskId = taskId ?: return
        viewModelScope.launch {
            toggleCompleteUseCase(safeTaskId, true)
            _navigationEvent.emit(NavigationEvent.NavigateBackWithResult(safeTaskId))
        }
    }

    fun toggleFavorite() {
        val safeTaskId = taskId ?: return
        viewModelScope.launch {
            val currentTask = _taskUiState.value.task ?: return@launch
            val isFavorite = !currentTask.favorite
            toggleTaskFavoriteUseCase(safeTaskId, isFavorite)
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
            updateTaskUseCase(currentTask)
            _snackbarEvent.emit(SnackbarEvent("Task updated"))
        }
    }

    fun onEnterEditMode() {
        _taskUiState.update { it.copy(isInEditMode = true) }
    }

    fun onExitEditMode() {
        // Only save if user was actually editing. Prevents saving stale data
        // when focus is lost due to navigation (e.g. going to RepeatScreen).
        if (_taskUiState.value.isInEditMode) {
            saveTitle()
        }
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
            updateTaskUseCase(currentDetail)
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
            updateTaskUseCase(currentTask)
        }
    }

    fun onClearDateSelected() {
        _taskUiState.update { currentTask ->
            currentTask.copy(task = currentTask.task?.copy(startDate = null))
        }

        val currentTask = _taskUiState.value.task ?: return

        viewModelScope.launch {
            updateTaskUseCase(currentTask)
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
            updateTaskUseCase(currentTask)
        }
    }

    fun onClearTimeSelected() {
        _taskUiState.update { currentTask ->
            currentTask.copy(task = currentTask.task?.copy(startTime = null))
        }

        val currentTask = _taskUiState.value.task ?: return

        viewModelScope.launch {
            updateTaskUseCase(currentTask)
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

    private fun addTaskToCalendar() {
        viewModelScope.launch {
            val task = uiState.value.task ?: return@launch
            val startTimeMillis = task.reminderTimeMillis

            if (startTimeMillis == null) {
                _snackbarEvent.emit(SnackbarEvent("Please set a reminder time for the task"))
                return@launch
            }

            val endTimeMillis = startTimeMillis + 3600 * 1000
            _effect.emit(TaskDetailEffect.OpenCalendar(task, startTimeMillis, endTimeMillis))
        }
    }

    fun onEvent(event: TaskDetailEvent) {
        when(event) {
            is TaskDetailEvent.AddToCalendar -> addTaskToCalendar()
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
                val safeTaskId = taskId ?: return
                viewModelScope.launch {
                    moveTaskToCollectionUseCase(safeTaskId, event.collectionId)
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

