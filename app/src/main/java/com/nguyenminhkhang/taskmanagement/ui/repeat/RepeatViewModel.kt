package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTaskUiState
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
    private  val taskRepo: TaskRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val taskId: Long = savedStateHandle.get<Long>("taskId") ?: 0L

    private val _taskUiState = MutableStateFlow(RepeatUiState())
    val uiState: StateFlow<RepeatUiState> = _taskUiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            taskRepo.getTaskById(taskId).collect { taskEntity ->
                _taskUiState.update {currentState ->
                    currentState.copy(
                        task = taskEntity.toTaskUiState(),
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun onRepeatEveryChanged(repeatEvery: Long) {
        viewModelScope.launch {
            taskRepo.updateTaskRepeatEveryById(taskId, repeatEvery)
        }
    }

    fun onIntervalSelected(interval: String) {
        viewModelScope.launch {
            taskRepo.updateTaskRepeatIntervalById(taskId, interval)
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
            taskRepo.clearTimeSelected(taskId)
        }
    }

    fun callHelloWorld(){
        println("Hello World from RepeatViewModel")
    }

    fun updateTaskRepeatById() {
        val currentState = uiState.value
        val taskToUpdate = currentState.task ?: return // Nếu task null thì không làm gì

        viewModelScope.launch {
            taskRepo.updateTaskRepeatById(
                taskId = taskId,
                repeatEvery = currentState.task.repeatEvery,
                repeatDaysOfWeek = currentState.task.repeatDaysOfWeek?.joinToString(",") ?: "",
                repeatInterval = currentState.task.repeatInterval,
                repeatStartDay = currentState.task.startDate,
                repeatEndType = currentState.selectedEndCondition,
                repeatEndDate = currentState.task.repeatEndDate,
                repeatEndCount = currentState.occurrenceCount.toIntOrNull() ?: 1,
                startTime = currentState.task.startTime ?: 0L
            )
            _navigationEvent.emit(NavigationEvent.NavigateBackWithResult(taskId))
        }
    }
}

sealed class RepeatEvent {
    data class MonthRepeatOptionChanged(val option: String) : RepeatEvent()
    data class DayInMonthChanged(val day: Int) : RepeatEvent()
    data class WeekOrderChanged(val order: String) : RepeatEvent()
    data class WeekDayChanged(val day: String) : RepeatEvent()

    data class WeekDayClicked(val day: String) : RepeatEvent()

    data class EndConditionChanged(val option: String) : RepeatEvent()
    object ShowEndDatePicker : RepeatEvent()
    object DismissEndDatePicker : RepeatEvent()
    data class OccurrenceCountChanged(val count: String) : RepeatEvent()
}