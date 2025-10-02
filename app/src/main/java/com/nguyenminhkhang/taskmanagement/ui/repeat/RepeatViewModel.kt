package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatUiState
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.NavigationEvent
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
class RepeatViewModel @Inject constructor(
    private  val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val taskId: Long = savedStateHandle.get<Long>("taskId") ?: 0L

    private val _taskUiState = MutableStateFlow(RepeatUiState())
    val uiState: StateFlow<RepeatUiState> = _taskUiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            taskRepository.getTaskById(taskId).collect { taskEntity ->
                _taskUiState.update {currentState ->
                    currentState.copy(
                        task = taskEntity,
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun onRepeatEveryChanged(repeatEvery: Long) {
        _taskUiState.update { currentState ->
            currentState.copy(
                task = currentState.task?.copy(repeatEvery = repeatEvery)
            )
        }
        val currentTask = _taskUiState.value.task ?: return
        viewModelScope.launch {
            taskRepository.updateTask(currentTask)
        }
    }

    fun onIntervalSelected(interval: String) {
        _taskUiState.update { currentState ->
            currentState.copy(
                isIntervalDropdownVisible = false,
                task = currentState.task?.copy(repeatInterval = interval)
            )
        }
        val currentTask = _taskUiState.value.task ?: return
        viewModelScope.launch {
            taskRepository.updateTask(currentTask)
        }
    }

    fun onIntervalDropdownDismiss() {
        _taskUiState.update { currentState ->
            currentState.copy(isIntervalDropdownVisible = false)
        }
    }

    fun onIntervalDropdownClicked() {
        _taskUiState.update { currentState ->
            currentState.copy(isIntervalDropdownVisible = !currentState.isIntervalDropdownVisible)
        }
    }
    // date picker
    fun onDateSelected(dateInMillis: Long) {
        _taskUiState.update { currentTask ->
            currentTask.copy( task = currentTask.task?.copy(startDate = dateInMillis), isDatePickerVisible = false)
        }
    }

    fun onEndDateSelected(dateInMillis: Long) {
        _taskUiState.update { currentTask ->
            currentTask.copy(task = currentTask.task?.copy(repeatEndDate = dateInMillis), isEndDatePickerVisible = false)
        }
    }

    fun onClearDateSelected() {
        _taskUiState.update { currentTask ->
            currentTask.copy(task = currentTask.task?.copy(startDate = null))
        }
        val currentTask = _taskUiState.value.task ?: return
        viewModelScope.launch {
            taskRepository.updateTask(currentTask)
        }
    }

    fun onShowDatePicker() {
        _taskUiState.update { it.copy(isDatePickerVisible = true) }
    }

    fun onDismissDatePicker() {
        _taskUiState.update { it.copy(isDatePickerVisible = false) }
    }

    // time picker
    fun onTimeSelected(timeInMillis: Long) {
        _taskUiState.update { currentTask ->
            currentTask.copy(task = currentTask.task?.copy(startTime = timeInMillis), isTimePickerVisible = false)
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

    fun onClearTimeSelected() {
        viewModelScope.launch {
            taskRepository.clearTimeSelected(taskId)
        }
    }

    fun updateRepeatTask() {
        val currentState = uiState.value
        val taskToUpdate = currentState.task ?: return // Nếu task null thì không làm gì

        viewModelScope.launch {
            taskRepository.updateTask(taskToUpdate)
            _navigationEvent.emit(NavigationEvent.NavigateBackWithResult(taskId))
        }
    }

    fun onEvent(event: RepeatEvent) {
        when (event) {
            is RepeatEvent.MonthRepeatOptionChanged -> {
                _taskUiState.update { it.copy(selectedMonthRepeatOption = event.option) }
            }
            is RepeatEvent.DayInMonthChanged -> {
                _taskUiState.update { it.copy(selectedDayInMonth = event.day) }
            }
            is RepeatEvent.WeekOrderChanged -> {
                _taskUiState.update { it.copy(selectedWeekOrder = event.order) }
            }
            is RepeatEvent.WeekDayChanged -> {
                _taskUiState.update { it.copy( selectedWeekDay = event.day) }
            }
            is RepeatEvent.EndConditionChanged -> {
                _taskUiState.update { it.copy(selectedEndCondition = event.option) }
            }
            is RepeatEvent.ShowEndDatePicker -> {
                _taskUiState.update { it.copy(isEndDatePickerVisible = true) }
            }
            is RepeatEvent.DismissEndDatePicker -> {
                _taskUiState.update { it.copy(isEndDatePickerVisible = false) }
            }
            is RepeatEvent.OccurrenceCountChanged -> {
                if( event.count.all { it.isDigit()} ) {
                    _taskUiState.update { it.copy(occurrenceCount = event.count) }
                } else {
                    // Ignore non-digit input
                    _taskUiState.update { it.copy(occurrenceCount = "1") }
                }
            }
            is RepeatEvent.WeekDayClicked -> {
                _taskUiState.update {currentState ->
                    val currentDays = currentState.task?.repeatDaysOfWeek.orEmpty()
                    val updatedDays = currentDays.toMutableList()

                    if (updatedDays.contains(event.day)) {
                        updatedDays.remove(event.day)
                    } else {
                        updatedDays.add(event.day)
                    }

                    currentState.copy(
                        task = currentState.task?.copy(
                            repeatDaysOfWeek = updatedDays.distinct() // đảm bảo không trùng
                        )
                    )
                }
            }
            is RepeatEvent.OnRepeatEveryChanged -> onRepeatEveryChanged(event.repeatEvery)
            is RepeatEvent.OnIntervalSelected -> onIntervalSelected(event.intervalSelected)
            is RepeatEvent.OnIntervalDropdownDismiss -> onIntervalDropdownDismiss()
            is RepeatEvent.OnIntervalDropdownClicked -> onIntervalDropdownClicked()
            is RepeatEvent.OnShowTimePicker -> onShowTimePicker()
            is RepeatEvent.OnDismissTimePicker -> onDismissTimePicker()
            is RepeatEvent.OnClearTimeSelected -> onClearTimeSelected()
            is RepeatEvent.OnTimeSelected -> onTimeSelected(event.timeMillis)
            is RepeatEvent.OnShowStartDatePicker -> onShowDatePicker()
            is RepeatEvent.OnDismissStartDatePicker -> onDismissDatePicker()
            is RepeatEvent.OnClearStartDateSelected -> onClearDateSelected()
            is RepeatEvent.OnStartDateSelected -> onDateSelected(event.dateMillis)
            is RepeatEvent.OnEndDateSelected -> onEndDateSelected(event.dateMillis)
            is RepeatEvent.OnSaveRepeatTaskSetup -> updateRepeatTask()
        }
    }
}