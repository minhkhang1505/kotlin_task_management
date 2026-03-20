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
import com.nguyenminhkhang.taskmanagement.ui.home.event.CollectionEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.HomeEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.MenuEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.TaskEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.UiEvent
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

data class TaskUseCases @Inject constructor(
    val addTask: AddTaskUseCase,
    val toggleFavorite: ToggleTaskFavoriteUseCase,
    val toggleComplete: ToggleCompleteUseCase,
    val deleteTask: DeleteTaskUseCase,
    val syncTasks: SyncTasksUseCase
)

data class CollectionUseCases @Inject constructor(
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

    private var _menuEvent = MutableSharedFlow<List<AppMenuItem>>()
    val menuEvent = _menuEvent.asSharedFlow()

    private var _currentSelectedCollectionId:Long = -1L
    val stringProvider: StringProvider get() = strings


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

    private fun deleteSelectedTask(taskId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            taskUseCases.deleteTask.invoke(taskId)
        }
    }

    private fun requestUpdateCollection(collectionId: Long) {
        val actionsList = listOf(
            if(!_uiState.value.isShowDeleteButtonVisible) AppMenuItem(title = strings.getString(R.string.delete_task)) { onEvent(
                UiEvent.ShowDeleteButton) } else {
                AppMenuItem(title = "Done") { onEvent(UiEvent.HideDeleteButton) }
            },
            AppMenuItem(title = strings.getString(R.string.delete_collection)) {
                deleteCollectionById(collectionId) },
            AppMenuItem(title = strings.getString((R.string.rename_collection))) {
                onEvent(CollectionEvent.ClearRenameCollectionName)
                onEvent(CollectionEvent.ShowRenameCollectionDialog)
            }
        )
//        _uiState.update {
//            it.copy(sortMenuButtonSheet = actionsList)
//        }
    }

    private fun sortTaskCollection(collectionId: Long, sortedType: SortedType) {
        viewModelScope.launch {
            collectionUseCases.updateSortType.invoke(collectionId, sortedType)
        }
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

    private fun closeRenameCollectionDialog() {
        _uiState.update { it.copy(isNewCollectionNameDialogVisible = false) }
    }

    private fun showRenameCollectionDialog() {
        _uiState.update { it.copy(isNewCollectionNameDialogVisible = true) }
    }

    private fun clearRenameCollectionName() {
        _uiState.update { it.copy(newCollectionName = "") }
    }

    private fun changeCollectionName(name: String) {
        _uiState.update { it.copy(newTaskCollectionName = name) }
    }

    private fun renameCollection(newCollectionName: String) {
        viewModelScope.launch {
            collectionUseCases.updateName.invoke(_currentSelectedCollectionId, newCollectionName)
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is UiEvent.ShowAddTaskSheet -> _uiState.update {
                if ( _currentSelectedCollectionId>0L ) {
                    it.copy(isAddTaskSheetVisible = true)
                } else {
                    it.copy(isAddTaskSheetVisible = false)
                }
            }
            is UiEvent.HideAddTaskSheet -> _uiState.update { it.copy(isAddTaskSheetVisible = false) }
            is TaskEvent.TaskContentChanged -> _uiState.update { it.copy(newTask = it.newTask!!.copy(content = event.content)) }
            is TaskEvent.TaskDetailChanged -> _uiState.update { it.copy(newTask = it.newTask!!.copy(taskDetail = event.detail)) }
            is TaskEvent.ToggleNewTaskFavorite -> _uiState.update { it.copy(newTask = it.newTask!!.copy(favorite = !it.newTask.favorite)) }
            is UiEvent.ShowAddDetailTextField -> _uiState.update { it.copy(isShowAddDetailTextField = true) }
            is UiEvent.ShowDatePicker -> _uiState.update { it.copy(isDatePickerVisible = true) }
            is UiEvent.HideDatePicker -> _uiState.update { it.copy(isDatePickerVisible = false) }
            is UiEvent.DateSelected -> _uiState.update { it.copy(newTask = it.newTask!!.copy(startDate = event.date)) }
            is UiEvent.ShowTimePicker -> _uiState.update { it.copy(isTimePickerVisible = true) }
            is UiEvent.HideTimePicker -> _uiState.update { it.copy(isTimePickerVisible = false) }
            is UiEvent.TimeSelected -> _uiState.update { it.copy(newTask = it.newTask!!.copy(startTime = event.time)) }
            is TaskEvent.SelectedDateTimeCleared -> _uiState.update {
                it.copy(
                    newTask = it.newTask!!.copy(startDate = null, startTime = null),
                    selectedReminderHour = null,
                    selectedReminderMinute = null
                )
            }
            is TaskEvent.SaveNewTask -> addNewTask()
            is TaskEvent.NewTaskCleared -> _uiState.update {
                it.copy(
                    newTask = TaskEntity(content = "")
                )
            }
            is TaskEvent.ToggleFavorite -> handleToggleFavorite(event.task)
            is TaskEvent.ToggleComplete -> handleToggleComplete(event.task)
            is CollectionEvent.UpdateCollectionRequested -> requestUpdateCollection(event.collectionId)
            is MenuEvent.ResetMenuListButtonSheet -> _uiState.update { it.copy(sortMenuButtonSheet = null) }
            is UiEvent.ShowAddNewCollectionButton -> _uiState.update { it.copy(isShowAddNewCollectionSheetVisible = true) }
            is UiEvent.HideAddNewCollectionButton -> _uiState.update { it.copy(isShowAddNewCollectionSheetVisible = false) }
            is CollectionEvent.CurrentCollectionId -> updateCurrentCollectionId(event.collectionId)
            is CollectionEvent.AddNewCollectionRequested -> requestAddNewCollection(event.name)
            is CollectionEvent.OnCollectionNameChanged -> changeCollectionName(event.name)
            is CollectionEvent.NewCollectionNameCleared -> _uiState.update { it.copy(newTaskCollectionName = "") }
            is MenuEvent.RequestShowButtonSheetOption -> {}
//                _uiState.update {
//                it.copy(sortMenuButtonSheet = event.list)
//            }
            is TaskEvent.UndoToggleComplete -> undoToggleComplete()
            is TaskEvent.UpdateReminderTimeMillis -> _uiState.update{ it.copy(newTask = it.newTask!!.copy(reminderTimeMillis = event.reminder)) }
            is TaskEvent.OnReminderTimeSelected -> onReminderTimeSelected(event.hour, event.minute)
            is TaskEvent.CombineDateAndTime -> combineDateAndTime(dateMillis = event.date, hour = event.hour, minute = event.minute)?.let { reminderTimeMillis ->
                _uiState.update { it.copy(newTask = it.newTask!!.copy(reminderTimeMillis = reminderTimeMillis)) }
            }
            is UiEvent.ShowDeleteButton -> _uiState.update { it.copy(isShowDeleteButtonVisible = true) }
            is UiEvent.HideDeleteButton -> _uiState.update { it.copy(isShowDeleteButtonVisible = false) }
            is TaskEvent.DeleteTask -> deleteSelectedTask(event.taskId)
            is CollectionEvent.ShowRenameCollectionDialog -> showRenameCollectionDialog()
            is CollectionEvent.HideRenameCollectionDialog -> closeRenameCollectionDialog()
            is CollectionEvent.ClearRenameCollectionName -> clearRenameCollectionName()
            is CollectionEvent.RenameCollection -> renameCollection(event.newCollectionName)
            is MenuEvent.SortCollection -> sortTaskCollection(event.collectionId, event.sortedType)
            is CollectionEvent.DeleteCollection -> deleteCollectionById(event.collectionId)
        }
    }
}