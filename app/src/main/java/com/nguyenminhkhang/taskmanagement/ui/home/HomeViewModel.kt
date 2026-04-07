package com.nguyenminhkhang.taskmanagement.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsEvent
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsTracker
import com.nguyenminhkhang.taskmanagement.domain.model.SortedType
import com.nguyenminhkhang.taskmanagement.domain.model.Task
import com.nguyenminhkhang.taskmanagement.notification.TaskScheduler
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
import com.nguyenminhkhang.taskmanagement.domain.usecase.home.CombineDateAndTimeUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.syncusecase.SyncTasksUseCase
import com.nguyenminhkhang.taskmanagement.ui.common.stringprovider.StringProvider
import com.nguyenminhkhang.taskmanagement.ui.home.event.CollectionEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.HomeEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.MenuEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.TaskEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.UiEvent
import com.nguyenminhkhang.taskmanagement.ui.home.state.HomeUiState
import com.nguyenminhkhang.taskmanagement.ui.common.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.common.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.common.snackbar.SnackbarActionType
import com.nguyenminhkhang.taskmanagement.ui.common.snackbar.SnackbarEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber
import javax.inject.Inject

const val ID_ADD_NEW_LIST = -999L
const val ID_ADD_FAVORITE_LIST = -1000L

data class TaskUseCases @Inject constructor(
    val addTask: AddTaskUseCase,
    val toggleFavorite: ToggleTaskFavoriteUseCase,
    val toggleComplete: ToggleCompleteUseCase,
    val deleteTask: DeleteTaskUseCase,
    val syncTasks: SyncTasksUseCase,
    val combineDateAndTime: CombineDateAndTimeUseCase
)

data class CollectionUseCases @Inject constructor(
    val getGroups: GetTaskGroupsUseCase,
    val getCollections: GetTaskCollectionsUseCase,
    val addCollection: AddNewCollectionUseCase,
    val deleteCollection: DeleteTaskCollectionUseCase,
    val updateSortType: UpdateCollectionSortTypeUseCase,
    val updateName: UpdateCollectionNameUseCase
)

class HomeViewModel(
    private val taskUseCases: TaskUseCases,
    private val collectionUseCases: CollectionUseCases,
    private val scheduler: TaskScheduler,
    private val strings: StringProvider,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _snackBarEvent = Channel<SnackbarEvent>(Channel.BUFFERED)
    val snackBarEvent = _snackBarEvent.receiveAsFlow()

    private var pendingCompleteAction : Job? = null
    private var taskToConfirm : TaskUiState? = null

    private val currentSelectedCollectionId: Long
        get() = _uiState.value.currentCollectionId

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

        viewModelScope.launch {
            if (collectionUseCases.getCollections.invoke().first().isEmpty()) {
                collectionUseCases.addCollection.invoke(strings.getString(R.string.collection_one))
                collectionUseCases.addCollection.invoke(strings.getString(R.string.collection_two))
            }

            taskUseCases.syncTasks.invoke()
        }

        Timber.tag(TAG).d("HomeViewModel created")
    }

    fun onScreenShown() {
        analyticsTracker.trackEvent(
            AnalyticsEvent.ScreenView("HomeScreen")
        )
    }

    private fun addNewTask() {
        val taskToSave = _uiState.value
        if (taskToSave.newTask!!.content.isBlank() || currentSelectedCollectionId <= 0) return

        val content = taskToSave.newTask.content.trim()
        val detail = taskToSave.newTask.taskDetail
        val isFavorite = taskToSave.newTask.favorite
        val startDate = taskToSave.newTask.startDate
        val startTime = taskToSave.newTask.startTime
        val reminderTimeMillis = taskToSave.newTask.reminderTimeMillis

        viewModelScope.launch {
            runCatching {
                taskUseCases.addTask.invoke(
                    content = content,
                    collectionId = currentSelectedCollectionId,
                    taskDetail = detail,
                    isFavorite = isFavorite,
                    startDate = startDate,
                    startTime = startTime,
                    reminderTimeMillis = reminderTimeMillis
                )
            }.onSuccess{ task ->
                task ?: return@onSuccess

                if (taskToSave.newTask.reminderTimeMillis != null) {
                    scheduler.schedule(task)
                } else {
                    scheduler.cancel(task)
                }
                task.id?.let { taskId ->
                    analyticsTracker.trackEvent(
                        AnalyticsEvent.AddTask(taskId)
                    )
                }
            }.onFailure {
                Timber.e(it, "Add task fail")
            }
        }
    }

    private fun handleToggleFavorite(task: TaskUiState) {
        viewModelScope.launch {
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
            _snackBarEvent.send(SnackbarEvent(
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

        viewModelScope.launch {
            taskUseCases.toggleComplete.invoke(task.id!!, !task.completed)
        }
        taskToConfirm = null
    }

    private fun createOptimisticListForToggle(taskToChange: TaskUiState): List<TaskGroupUiState> {
        val currentList = _uiState.value.listTabGroup
        val isCompleting = !taskToChange.completed

        return currentList.map { group->
            if(group.tab.id == currentSelectedCollectionId) {
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
        Timber.tag(TAG).d("updateCurrentCollectionId() - with id: ${collectionId}")
        _uiState.update { it.copy(currentCollectionId = collectionId) }
        Timber.tag(TAG).d("After update uiState.currentCollectionId: ${_uiState.value.currentCollectionId}")
    }

    private fun deleteCollectionById(collectionId: Long) {
        Timber.tag(TAG).d("Current collection: ${currentSelectedCollectionId}")
        Timber.tag(TAG).d("deleteCollectionById() - with ID: ${collectionId}")

        viewModelScope.launch {
            runCatching {
                collectionUseCases.deleteCollection.invoke(collectionId)
            }.onSuccess {
                analyticsTracker.trackEvent(
                    AnalyticsEvent.DeleteCollection(collectionId)
                )
            }.onFailure {
                Timber.e(it,"Delete collection fail")
            }
        }
    }

    private fun requestAddNewCollection(name: String) {
        val newCollectionName = name
        if (newCollectionName.isBlank()) return

        viewModelScope.launch {
            runCatching {
                collectionUseCases.addCollection.invoke(newCollectionName)
            }.onSuccess {
                analyticsTracker.trackEvent(
                    AnalyticsEvent.CreateCollection(name)
                )
            }.onFailure {
                Timber.e(it, " Create new collection fail")
            }
        }
    }

    private fun deleteSelectedTask(taskId: Long) {
        viewModelScope.launch {
            runCatching {
                taskUseCases.deleteTask.invoke(taskId)
            }.onSuccess {
                analyticsTracker.trackEvent(
                    AnalyticsEvent.DeleteTask(taskId)
                )
            }.onFailure{
                Timber.e(it, "Delete task fail")
            }
        }
    }

    private fun requestUpdateCollection(collectionId: Long) {
//        val actionsList = listOf(
//            if(!_uiState.value.isShowDeleteButtonVisible) AppMenuItem(title = strings.getString(R.string.delete_task)) { onEvent(
//                UiEvent.ShowDeleteButton) } else {
//                AppMenuItem(title = "Done") { onEvent(UiEvent.HideDeleteButton) }
//            },
//            AppMenuItem(title = strings.getString(R.string.delete_collection)) {
//                deleteCollectionById(collectionId) },
//            AppMenuItem(title = strings.getString((R.string.rename_collection))) {
//                onEvent(CollectionEvent.ClearRenameCollectionName)
//                onEvent(CollectionEvent.ShowRenameCollectionDialog)
//            }
//        )
//        _uiState.update {
//            it.copy(sortMenuButtonSheet = actionsList)
//        }
    }

    private fun sortTaskCollection(collectionId: Long, sortedType: SortedType) {
        Timber.tag(TAG).d("sortTaskCollection() - collectionId: ${collectionId}, sortType: ${sortedType}")
        viewModelScope.launch {
            collectionUseCases.updateSortType.invoke(collectionId, sortedType)
        }
        Timber.tag(TAG).d("sortTaskCollection() - After call use-case")

    }

    fun onReminderTimeSelected(hour: Int, minute: Int) {
        _uiState.update { it.copy(selectedReminderHour = hour, selectedReminderMinute = minute) }
    }

    private fun closeRenameCollectionDialog() {
        _uiState.update { it.copy(isNewCollectionNameDialogVisible = false) }
    }

    private fun showRenameCollectionDialog() {
        _uiState.update { it.copy(isNewCollectionNameDialogVisible = true) }
    }

    private fun clearRenameCollectionName() {
        Timber.tag(TAG).d("clearRenameCollectionName() - CLEAR NAME TRIGGERED")
        _uiState.update { it.copy(newCollectionName = "") }
    }

    private fun changeCollectionName(name: String) {

        Timber.tag(TAG).d("Enter new collection name: ${name}")
        _uiState.update { it.copy(newCollectionName = name) }
        Timber.tag(TAG).d("After update new collection name: ${_uiState.value.newCollectionName}")
    }

    private fun renameCollection(newCollectionName: String) {
        viewModelScope.launch {
            runCatching {
                collectionUseCases.updateName.invoke(
                    currentSelectedCollectionId,
                    newCollectionName
                )
            }.onSuccess {
                analyticsTracker.trackEvent(
                    AnalyticsEvent.RenameCollection(currentSelectedCollectionId)
                )
            }.onFailure{
                Timber.e(it, "Rename collection fail")
            }
        }
    }

    private fun handleUiEvent(event: UiEvent) {
        when(event) {
            is UiEvent.ShowAddTaskSheet -> _uiState.update {
                if (currentSelectedCollectionId > 0L) {
                    it.copy(isAddTaskSheetVisible = true)
                } else {
                    it.copy(isAddTaskSheetVisible = false)
                }
            }
            is UiEvent.HideAddTaskSheet -> _uiState.update { it.copy(isAddTaskSheetVisible = false) }
            is UiEvent.ShowAddDetailTextField -> _uiState.update { it.copy(isShowAddDetailTextField = true) }
            is UiEvent.ShowDatePicker -> _uiState.update { it.copy(isDatePickerVisible = true) }
            is UiEvent.HideDatePicker -> _uiState.update { it.copy(isDatePickerVisible = false) }
            is UiEvent.DateSelected -> _uiState.update { it.copy(newTask = it.newTask!!.copy(startDate = event.date)) }
            is UiEvent.ShowTimePicker -> _uiState.update { it.copy(isTimePickerVisible = true) }
            is UiEvent.HideTimePicker -> _uiState.update { it.copy(isTimePickerVisible = false) }
            is UiEvent.TimeSelected -> _uiState.update { it.copy(newTask = it.newTask!!.copy(startTime = event.time)) }
            is UiEvent.ShowAddNewCollectionButton -> _uiState.update { it.copy(isShowAddNewCollectionSheetVisible = true) }
            is UiEvent.HideAddNewCollectionButton -> _uiState.update { it.copy(isShowAddNewCollectionSheetVisible = false) }
//            is UiEvent.ShowDeleteButton -> _uiState.update { it.copy(isShowDeleteButtonVisible = true) }
//            is UiEvent.HideDeleteButton -> _uiState.update { it.copy(isShowDeleteButtonVisible = false) }
            is UiEvent.OnToggleDeleteButton -> _uiState.update { currentTask->
                val deleteButtonState = currentTask.isShowDeleteButtonVisible
                if (deleteButtonState) {
                    currentTask.copy(isShowDeleteButtonVisible = false)
                } else {
                    currentTask.copy(isShowDeleteButtonVisible = true)
                }
            }
        }
    }

    private fun handleTaskEvent(event: TaskEvent) {
        when(event) {
            is TaskEvent.SelectedDateTimeCleared -> _uiState.update {
                it.copy(
                    newTask = it.newTask!!.copy(startDate = null, startTime = null),
                    selectedReminderHour = null,
                    selectedReminderMinute = null
                )
            }
            is TaskEvent.ToggleNewTaskFavorite -> _uiState.update { it.copy(newTask = it.newTask!!.copy(favorite = !it.newTask.favorite)) }
            is TaskEvent.TaskDetailChanged -> _uiState.update { it.copy(newTask = it.newTask!!.copy(taskDetail = event.detail)) }
            is TaskEvent.SaveNewTask -> addNewTask()
            is TaskEvent.NewTaskCleared -> _uiState.update {
                it.copy(
                    newTask = Task(content = "")
                )
            }
            is TaskEvent.ToggleFavorite -> handleToggleFavorite(event.task)
            is TaskEvent.ToggleComplete -> handleToggleComplete(event.task)
            is TaskEvent.TaskContentChanged -> _uiState.update { it.copy(newTask = it.newTask!!.copy(content = event.content)) }
//                _uiState.update {
//                it.copy(sortMenuButtonSheet = event.list)
//            }
            is TaskEvent.UndoToggleComplete -> undoToggleComplete()
            is TaskEvent.UpdateReminderTimeMillis -> _uiState.update{ it.copy(newTask = it.newTask!!.copy(reminderTimeMillis = event.reminder)) }
            is TaskEvent.OnReminderTimeSelected -> onReminderTimeSelected(event.hour, event.minute)
            is TaskEvent.CombineDateAndTime -> taskUseCases.combineDateAndTime(dateMillis = event.date, hour = event.hour, minute = event.minute)?.let { reminderTimeMillis ->
                _uiState.update { it.copy(newTask = it.newTask!!.copy(reminderTimeMillis = reminderTimeMillis)) }
            }
            is TaskEvent.DeleteTask -> deleteSelectedTask(event.taskId)
        }
    }

    private fun handleCollectionEvent(event: CollectionEvent) {
        when (event) {
            is CollectionEvent.DeleteCollection -> deleteCollectionById(event.collectionId)
            is CollectionEvent.UpdateCollectionRequested -> requestUpdateCollection(event.collectionId)
            is CollectionEvent.CurrentCollectionId -> updateCurrentCollectionId(event.collectionId)
            is CollectionEvent.AddNewCollectionRequested -> requestAddNewCollection(event.name)
            is CollectionEvent.ShowRenameCollectionDialog -> showRenameCollectionDialog()
            is CollectionEvent.HideRenameCollectionDialog -> closeRenameCollectionDialog()
            is CollectionEvent.ClearRenameCollectionName -> clearRenameCollectionName()
            is CollectionEvent.RenameCollection -> renameCollection(event.newCollectionName)
            is CollectionEvent.OnCollectionNameChanged -> changeCollectionName(event.name)
            is CollectionEvent.NewCollectionNameCleared -> _uiState.update { it.copy(newCollectionName = "") }
        }
    }

    private fun handleMenuEvent(event: MenuEvent) {
        when(event) {
            is MenuEvent.RequestShowButtonSheetOption -> {
                _uiState.update{ it.copy(

                )}
            }
            is MenuEvent.SortCollection -> sortTaskCollection(event.collectionId, event.sortedType)
            is MenuEvent.ResetMenuListButtonSheet -> _uiState.update { it.copy(sortMenuButtonSheet = null) }
            is MenuEvent.DismissSortDialog -> _uiState.update { it.copy(isSortDialogVisible = false) }
            is MenuEvent.ShowSortDialog -> _uiState.update { it.copy(isSortDialogVisible = true)}
            is MenuEvent.ShowActionBottomSheet -> _uiState.update { it.copy(isActionBottomSheetVisible = true) }
            is MenuEvent.DismissActionBottomSheet -> _uiState.update { it.copy(isActionBottomSheetVisible = false) }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is UiEvent -> handleUiEvent(event)
            is TaskEvent -> handleTaskEvent(event)
            is CollectionEvent -> handleCollectionEvent(event)
            is MenuEvent -> handleMenuEvent(event)
        }
    }
}