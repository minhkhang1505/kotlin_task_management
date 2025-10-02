package com.nguyenminhkhang.taskmanagement.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.domain.model.SortedType
import com.nguyenminhkhang.taskmanagement.notice.TaskScheduler
import com.nguyenminhkhang.taskmanagement.domain.usecase.AddTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.DeleteTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.GetTaskGroupsUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.ToggleCompleteUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.ToggleTaskFavoriteUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.AddNewCollectionUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.DeleteTaskCollectionUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.GetTaskCollectionsUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.UpdateCollectionNameUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.UpdateCollectionSortTypeUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.syncusecase.SyncTasksUseCase
import com.nguyenminhkhang.taskmanagement.ui.AppMenuItem
import com.nguyenminhkhang.taskmanagement.ui.common.stringprovider.StringProvider
import com.nguyenminhkhang.taskmanagement.ui.home.HomeEvent.ShowAddTaskSheet
import com.nguyenminhkhang.taskmanagement.ui.home.state.HomeUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarActionType
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

const val ID_ADD_NEW_LIST = -999L
const val ID_ADD_FAVORITE_LIST = -1000L

data class TaskUseCases(
    val addTask: AddTaskUseCase,
    val toggleFavorite: ToggleTaskFavoriteUseCase,
    val toggleComplete: ToggleCompleteUseCase,
    val deleteTask: DeleteTaskUseCase,
    val syncTasks: SyncTasksUseCase
)

data class CollectionUseCases(
    val getGroups: GetTaskGroupsUseCase,
    val getCollections: GetTaskCollectionsUseCase,
    val addCollection: AddNewCollectionUseCase,
    val deleteCollection: DeleteTaskCollectionUseCase,
    val updateSortType: UpdateCollectionSortTypeUseCase,
    val updateName: UpdateCollectionNameUseCase
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases,
    private val collectionUseCases: CollectionUseCases,
    private val scheduler: TaskScheduler,
    private val strings: StringProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var _snackBarEvent = MutableSharedFlow<SnackbarEvent>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    private var pendingCompleteAction : Job? = null
    private var taskToConfirm : TaskUiState? = null

    private var _currentSelectedCollectionId:Long = -1L

    init {
        collectionUseCases.getGroups.invoke(
            favoriteTabName = strings.getString(R.string.favorite_tab_name),
            newTabName = strings.getString(R.string.add_new_collecion)
        )
            .onEach { finalTabs ->
                _uiState.update { it.copy(listTabGroup = finalTabs, isLoading = false) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            if (collectionUseCases.getCollections.invoke().first().isEmpty()) {
                collectionUseCases.addCollection.invoke(strings.getString(R.string.collection_one))
                collectionUseCases.addCollection.invoke(strings.getString(R.string.collection_two))
            }

            taskUseCases.syncTasks.invoke()
        }
    }

    private fun addNewTask() {
        val taskToSave = _uiState.value
        if (taskToSave.newTask!!.content.isBlank() || _currentSelectedCollectionId <= 0) return

        val content = taskToSave.newTask.content.trim()
        val detail = taskToSave.newTask.taskDetail
        val isFavorite = taskToSave.newTask.favorite
        val startDate = taskToSave.newTask.startDate
        val startTime = taskToSave.newTask.startTime
        val reminderTimeMillis = taskToSave.newTask.reminderTimeMillis

        viewModelScope.launch(Dispatchers.IO) {
            val insertedTask = taskUseCases.addTask.invoke(
                content = content,
                collectionId = _currentSelectedCollectionId,
                taskDetail = detail,
                isFavorite = isFavorite,
                startDate = startDate,
                startTime = startTime,
                reminderTimeMillis = reminderTimeMillis
            )

            if(insertedTask != null) {
                val updatedTask = insertedTask.copy(
                    content = content,
                    taskDetail = detail,
                    favorite = isFavorite,
                    startDate = startDate,
                    startTime = startTime,
                    reminderTimeMillis = reminderTimeMillis
                )

                if (taskToSave.newTask.reminderTimeMillis != null) {
                    scheduler.schedule(updatedTask)
                } else {
                    scheduler.cancel(updatedTask)
                }
            }
        }
    }

    private fun handleToggleFavorite(task: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            taskUseCases.toggleFavorite.invoke(taskId = task.id!!, isFavorite = !task.isFavorite)
        }
    }

    private fun handleToggleComplete(task: TaskUiState) {
        pendingCompleteAction?.cancel()
        taskToConfirm = null

        val optimisticList = createOptimisticListForToggle(task)
        _uiState.update {
            it.copy(listTabGroup = optimisticList)
        }

        taskToConfirm = task

        viewModelScope.launch {
            _snackBarEvent.emit(SnackbarEvent(
                message = "Task '${task.content}' marked as ${if (task.completed) "incomplete" else "complete"}",
                actionLabel = "Undo",
                actionType = SnackbarActionType.UNDO_TOGGLE_COMPLETE,
                onAction = { undoToggleComplete() }
            ))

            pendingCompleteAction = viewModelScope.launch {
                delay(5000L)
                confirmToggleComplete()
            }
        }
    }

    private fun undoToggleComplete() {
        pendingCompleteAction?.cancel()
        val taskToRestore = taskToConfirm ?: return

        val restoredList = createOptimisticListForToggle(taskToRestore.copy(completed = !taskToRestore.completed))
        _uiState.update { it.copy(listTabGroup = restoredList) }

        taskToConfirm = null
    }

    private fun confirmToggleComplete() {
        val task = taskToConfirm ?: return

        viewModelScope.launch(Dispatchers.IO) {
            taskUseCases.toggleComplete.invoke(task.id!!, !task.completed)
        }
        taskToConfirm = null
    }

    private fun createOptimisticListForToggle(taskToChange: TaskUiState): List<TaskGroupUiState> {
        val currentList = _uiState.value.listTabGroup
        val isCompleting = !taskToChange.completed

        return currentList.map { group->
            if(group.tab.id == _currentSelectedCollectionId) {
                val newActiveList = if(isCompleting) {
                    group.page.activeTaskList.filter { it.id != taskToChange.id }
                } else {
                    group.page.activeTaskList + taskToChange.copy(completed = isCompleting)
                }

                val newCompletedList = if(isCompleting) {
                    group.page.completedTaskList + taskToChange.copy(completed = isCompleting)
                } else {
                    group.page.completedTaskList.filter { it.id != taskToChange.id }
                }

                group.copy(
                    page = group.page.copy(
                        activeTaskList = newActiveList,
                        completedTaskList = newCompletedList
                    )
                )
            } else {
                group
            }
        }
    }

    private fun updateCurrentCollectionId(collectionId: Long) {
        _currentSelectedCollectionId = collectionId
    }

    private fun deleteCollectionById(collectionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionUseCases.deleteCollection.invoke(collectionId)
        }
    }

    private fun requestAddNewCollection(name: String) {
        val newCollectionName = name
        if (newCollectionName.isBlank()) return

        viewModelScope.launch {
            collectionUseCases.addCollection.invoke(newCollectionName)
        }
    }

    private fun DeleteSelectedTask(taskId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            taskUseCases.deleteTask.invoke(taskId)
        }
    }

    private fun requestUpdateCollection(collectionId: Long) {
        val actionsList = listOf(
            if(!_uiState.value.isShowDeleteButtonVisible) AppMenuItem(title = strings.getString(R.string.delete_task)) { onEvent(HomeEvent.ShowDeleteButton) } else {
                AppMenuItem(title = "Done") { onEvent(HomeEvent.HideDeleteButton) }
            },
            AppMenuItem(title = strings.getString(R.string.delete_collection)) {
                deleteCollectionById(collectionId) },
            AppMenuItem(title = strings.getString((R.string.rename_collection))) {
                onEvent(HomeEvent.ClearRenameCollectionName)
                onEvent(HomeEvent.ShowRenameCollectionDialog)
            }
        )
        _uiState.update {
            it.copy(menuListButtonSheet = actionsList)
        }
    }

    private fun sortTaskCollection(collectionId: Long, sortedType: SortedType) {
        viewModelScope.launch {
            collectionUseCases.updateSortType.invoke(collectionId, sortedType)
        }
    }

    private fun requestSortTasks(collectionId: Long) {
        val menuItems = listOf(
            AppMenuItem(title = strings.getString(R.string.sort_by_date)) {
                sortTaskCollection(collectionId, SortedType.SORTED_BY_DATE)
            },
            AppMenuItem(title = strings.getString(R.string.sort_by_favorite)) {
                sortTaskCollection(collectionId, SortedType.SORTED_BY_FAVORITE)
            }
        )
        _uiState.update {
            it.copy(menuListButtonSheet = menuItems)
        }
    }

    private fun NewCollectionNameChanged(name: String) {
        _uiState.update { it.copy(newTaskCollectionName = name) }
    }

    fun onReminderTimeSelected(hour: Int, minute: Int) {
        _uiState.update { it.copy(selectedReminderHour = hour, selectedReminderMinute = minute) }
    }

    private fun combineDateAndTime(
        dateMillis: Long?,
        hour: Int?,
        minute: Int?
    ): Long? {
        if (dateMillis == null) return null

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateMillis

        if (hour != null && minute != null) {
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
        }

        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }

    private fun CloseRenameCollectionDialog() {
        _uiState.update { it.copy(isNewCollectionNameDialogVisible = false) }
    }

    private fun ShowRenameCollectionDialog() {
        _uiState.update { it.copy(isNewCollectionNameDialogVisible = true) }
    }

    private fun ClearRenameCollectionName() {
        _uiState.update { it.copy(newCollectionName = "") }
    }

    private fun OnCollectionNameChange(newCollectionName: String) {
        _uiState.update { it.copy(newCollectionName = newCollectionName) }
    }

    private fun RenameCollection(newCollectionName: String) {
        viewModelScope.launch {
            collectionUseCases.updateName.invoke(_currentSelectedCollectionId, newCollectionName)
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is ShowAddTaskSheet -> _uiState.update {
                if ( _currentSelectedCollectionId>0L ) {
                    it.copy(isAddTaskSheetVisible = true)
                } else {
                    it.copy(isAddTaskSheetVisible = false)
                }
            }
            is HomeEvent.HideAddTaskSheet -> _uiState.update { it.copy(isAddTaskSheetVisible = false) }
            is HomeEvent.TaskContentChanged -> _uiState.update { it.copy(newTask = it.newTask!!.copy(content = event.content)) }
            is HomeEvent.TaskDetailChanged -> _uiState.update { it.copy(newTask = it.newTask!!.copy(taskDetail = event.detail)) }
            is HomeEvent.ToggleNewTaskFavorite -> _uiState.update { it.copy(newTask = it.newTask!!.copy(favorite = !it.newTask.favorite)) }
            is HomeEvent.ShowAddDetailTextField -> _uiState.update { it.copy(isShowAddDetailTextField = true) }
            is HomeEvent.ShowDatePicker -> _uiState.update { it.copy(isDatePickerVisible = true) }
            is HomeEvent.HideDatePicker -> _uiState.update { it.copy(isDatePickerVisible = false) }
            is HomeEvent.DateSelected -> _uiState.update { it.copy(newTask = it.newTask!!.copy(startDate = event.date)) }
            is HomeEvent.ShowTimePicker -> _uiState.update { it.copy(isTimePickerVisible = true) }
            is HomeEvent.HideTimePicker -> _uiState.update { it.copy(isTimePickerVisible = false) }
            is HomeEvent.TimeSelected -> _uiState.update { it.copy(newTask = it.newTask!!.copy(startTime = event.time)) }
            is HomeEvent.SelectedDateTimeCleared -> _uiState.update {
                it.copy(
                    newTask = it.newTask!!.copy(startDate = null, startTime = null),
                    selectedReminderHour = null,
                    selectedReminderMinute = null
                )
            }
            is HomeEvent.SaveNewTask -> addNewTask()
            is HomeEvent.NewTaskCleared -> _uiState.update {
                it.copy(
                    newTask = TaskEntity(content = "")
                )
            }
            is HomeEvent.ToggleFavorite -> handleToggleFavorite(event.task)
            is HomeEvent.ToggleComplete -> handleToggleComplete(event.task)
            is HomeEvent.RequestSortTasks -> requestSortTasks(event.collectionId)
            is HomeEvent.UpdateCollectionRequested -> requestUpdateCollection(event.collectionId)
            is HomeEvent.ResetMenuListButtonSheet -> _uiState.update { it.copy(menuListButtonSheet = null) }
            is HomeEvent.ShowAddNewCollectionButton -> _uiState.update { it.copy(isShowAddNewCollectionSheetVisible = true) }
            is HomeEvent.HideAddNewCollectionButton -> _uiState.update { it.copy(isShowAddNewCollectionSheetVisible = false) }
            is HomeEvent.CurrentCollectionId -> updateCurrentCollectionId(event.collectionId)
            is HomeEvent.AddNewCollectionRequested -> requestAddNewCollection(event.name)
            is HomeEvent.NewCollectionNameChanged -> NewCollectionNameChanged(event.name)
            is HomeEvent.NewCollectionNameCleared -> _uiState.update { it.copy(newTaskCollectionName = "") }
            is HomeEvent.RequestShowButtonSheetOption -> _uiState.update { it.copy(menuListButtonSheet = event.list) }
            is HomeEvent.UndoToggleComplete -> undoToggleComplete()
            is HomeEvent.UpdateReminderTimeMillis -> _uiState.update{ it.copy(newTask = it.newTask!!.copy(reminderTimeMillis = event.reminder)) }
            is HomeEvent.OnReminderTimeSelected -> onReminderTimeSelected(event.hour, event.minute)
            is HomeEvent.CombineDateAndTime -> combineDateAndTime(dateMillis = event.date, hour = event.hour, minute = event.minute)?.let { reminderTimeMillis ->
                _uiState.update { it.copy(newTask = it.newTask!!.copy(reminderTimeMillis = reminderTimeMillis)) }
            }
            is HomeEvent.ShowDeleteButton -> _uiState.update { it.copy(isShowDeleteButtonVisible = true) }
            is HomeEvent.HideDeleteButton -> _uiState.update { it.copy(isShowDeleteButtonVisible = false) }
            is HomeEvent.DeleteTask -> DeleteSelectedTask(event.taskId)
            is HomeEvent.ShowRenameCollectionDialog -> ShowRenameCollectionDialog()
            is HomeEvent.HideRenameCollectionDialog -> CloseRenameCollectionDialog()
            is HomeEvent.ClearRenameCollectionName -> ClearRenameCollectionName()
            is HomeEvent.OnCollectionNameChange -> OnCollectionNameChange(event.newCollectionName)
            is HomeEvent.RenameCollection -> RenameCollection(event.newCollectionName)
        }
    }
}