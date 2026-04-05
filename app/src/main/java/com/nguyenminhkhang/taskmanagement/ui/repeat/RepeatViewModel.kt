package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.domain.usecase.repeat.GetTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.repeat.UpdateRepeatTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.repeat.TrackRepeatScreenViewUseCase
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatConstants
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatUiState
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RepeatViewModel @Inject constructor(
    private val getTaskUseCase: GetTaskUseCase,
    private val updateRepeatTaskUseCase: UpdateRepeatTaskUseCase,
    private val trackRepeatScreenViewUseCase: TrackRepeatScreenViewUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val taskId: Long = savedStateHandle.get<Long>("taskId") ?: 0L

    private val _taskUiState = MutableStateFlow(RepeatUiState())
    val uiState: StateFlow<RepeatUiState> = _taskUiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        Timber.tag(TAG).d("init() - Loading task with taskId=$taskId")
        viewModelScope.launch {
            val taskEntity = getTaskUseCase(taskId).first()
            Timber.tag(TAG).d("init() - Task loaded: id=${taskEntity.id}, interval=${taskEntity.repeatInterval}, every=${taskEntity.repeatEvery}, endType=${taskEntity.repeatEndType}, endCount=${taskEntity.repeatEndCount}")
            _taskUiState.update { currentState ->
                currentState.copy(
                    originalTask = taskEntity,
                    draftTask = taskEntity,
                    isLoading = false,
                    selectedEndCondition = taskEntity.repeatEndType
                        ?: RepeatConstants.EndCondition.Never,
                    occurrenceCount = taskEntity.repeatEndCount.toString(),
                )
            }
            Timber.tag(TAG).d("init() - UiState initialized, isLoading=false")
        }
    }

    fun onScreenShown() {
        Timber.tag(TAG).d("onScreenShown() - Tracking screen view")
        trackRepeatScreenViewUseCase()
    }

    fun onEvent(event: RepeatEvent) {
        Timber.tag(TAG).d("onEvent() - Received event: ${event::class.simpleName}")
        when (event) {
            // --- Frequency ---
            is RepeatEvent.OnRepeatEveryChanged -> {
                Timber.tag(TAG).d("onEvent() - OnRepeatEveryChanged: repeatEvery=${event.repeatEvery}")
                updateDraftTask { it.copy(repeatEvery = event.repeatEvery) }
            }
            is RepeatEvent.OnIntervalSelected -> {
                Timber.tag(TAG).d("onEvent() - OnIntervalSelected: interval=${event.intervalSelected}")
                updateDraftTask { it.copy(repeatInterval = event.intervalSelected) }
            }

            // --- Weekly days ---
            is RepeatEvent.WeekDayClicked -> {
                Timber.tag(TAG).d("onEvent() - WeekDayClicked: day=${event.day}")
                _taskUiState.update { currentState ->
                    val currentDays = currentState.draftTask?.repeatDaysOfWeek.orEmpty()
                    val updatedDays = currentDays.toMutableList()

                    if (updatedDays.contains(event.day)) {
                        updatedDays.remove(event.day)
                    } else {
                        updatedDays.add(event.day)
                    }
                    val result = updatedDays.distinct()
                    Timber.tag(TAG).d("onEvent() - WeekDayClicked: updatedDays=$result")

                    currentState.copy(
                        draftTask = currentState.draftTask?.copy(
                            repeatDaysOfWeek = result
                        )
                    )
                }
            }

            // --- Monthly options ---
            is RepeatEvent.MonthRepeatOptionChanged -> {
                Timber.tag(TAG).d("onEvent() - MonthRepeatOptionChanged: option=${event.option}")
                _taskUiState.update { it.copy(selectedMonthRepeatOption = event.option) }
            }
            is RepeatEvent.DayInMonthChanged -> {
                Timber.tag(TAG).d("onEvent() - DayInMonthChanged: day=${event.day}")
                _taskUiState.update { it.copy(selectedDayInMonth = event.day) }
            }
            is RepeatEvent.WeekOrderChanged -> {
                Timber.tag(TAG).d("onEvent() - WeekOrderChanged: order=${event.order}")
                _taskUiState.update { it.copy(selectedWeekOrder = event.order) }
            }
            is RepeatEvent.WeekDayChanged -> {
                Timber.tag(TAG).d("onEvent() - WeekDayChanged: day=${event.day}")
                _taskUiState.update { it.copy(selectedWeekDay = event.day) }
            }

            // --- Time ---
            is RepeatEvent.OnTimeSelected -> {
                Timber.tag(TAG).d("onEvent() - OnTimeSelected: timeMillis=${event.timeMillis}")
                updateDraftTask { it.copy(startTime = event.timeMillis) }
            }
            is RepeatEvent.OnClearTimeSelected -> {
                Timber.tag(TAG).d("onEvent() - OnClearTimeSelected: clearing startTime")
                updateDraftTask { it.copy(startTime = null) }
            }

            // --- Start date ---
            is RepeatEvent.OnStartDateSelected -> {
                Timber.tag(TAG).d("onEvent() - OnStartDateSelected: dateMillis=${event.dateMillis}")
                updateDraftTask { it.copy(startDate = event.dateMillis) }
            }
            is RepeatEvent.OnClearStartDateSelected -> {
                Timber.tag(TAG).d("onEvent() - OnClearStartDateSelected: clearing startDate")
                updateDraftTask { it.copy(startDate = null) }
            }

            // --- End condition ---
            is RepeatEvent.EndConditionChanged -> {
                Timber.tag(TAG).d("onEvent() - EndConditionChanged: option=${event.option}")
                _taskUiState.update { it.copy(selectedEndCondition = event.option) }
            }
            is RepeatEvent.OnEndDateSelected -> {
                Timber.tag(TAG).d("onEvent() - OnEndDateSelected: dateMillis=${event.dateMillis}")
                updateDraftTask { it.copy(repeatEndDate = event.dateMillis) }
            }
            is RepeatEvent.OccurrenceCountChanged -> {
                Timber.tag(TAG).d("onEvent() - OccurrenceCountChanged: count=${event.count}")
                if (event.count.all { it.isDigit() }) {
                    _taskUiState.update { it.copy(occurrenceCount = event.count) }
                } else {
                    Timber.tag(TAG).w("onEvent() - OccurrenceCountChanged: invalid input '${event.count}', resetting to 1")
                    _taskUiState.update { it.copy(occurrenceCount = "1") }
                }
            }

            // --- Save ---
            is RepeatEvent.OnSaveRepeatTaskSetup -> {
                Timber.tag(TAG).d("onEvent() - OnSaveRepeatTaskSetup: starting save")
                saveRepeatTask()
            }
        }
    }

    private fun saveRepeatTask() {
        val currentState = uiState.value
        val draft = currentState.draftTask ?: run {
            Timber.tag(TAG).w("saveRepeatTask() - draftTask is null, aborting save")
            return
        }

        val taskToSave = draft.copy(
            repeatEndType = currentState.selectedEndCondition,
            repeatEndCount = currentState.occurrenceCount.toIntOrNull() ?: 1,
            repeatEndDate = if (currentState.selectedEndCondition == RepeatConstants.EndCondition.At) {
                draft.repeatEndDate
            } else {
                null
            },
            updatedAt = System.currentTimeMillis()
        )

        Timber.tag(TAG).d("saveRepeatTask() - Saving task: id=${taskToSave.id}, interval=${taskToSave.repeatInterval}, every=${taskToSave.repeatEvery}, endType=${taskToSave.repeatEndType}, endCount=${taskToSave.repeatEndCount}, endDate=${taskToSave.repeatEndDate}, startDate=${taskToSave.startDate}, startTime=${taskToSave.startTime}, daysOfWeek=${taskToSave.repeatDaysOfWeek}")

        viewModelScope.launch {
            val success = updateRepeatTaskUseCase(taskToSave)
            Timber.tag(TAG).d("saveRepeatTask() - UpdateRepeatTaskUseCase returned: success=$success")
            _navigationEvent.emit(NavigationEvent.NavigateBackWithResult(taskId))
            Timber.tag(TAG).d("saveRepeatTask() - NavigateBackWithResult emitted for taskId=$taskId")
        }
    }

    private inline fun updateDraftTask(crossinline transform: (com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity) -> com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity) {
        _taskUiState.update { currentState ->
            val updated = currentState.draftTask?.let { transform(it) }
            Timber.tag(TAG).d("updateDraftTask() - Draft updated: id=${updated?.id}, interval=${updated?.repeatInterval}, every=${updated?.repeatEvery}")
            currentState.copy(draftTask = updated)
        }
    }

    companion object {
        private const val TAG = "RepeatViewModel"
    }
}