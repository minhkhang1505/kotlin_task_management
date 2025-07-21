package com.nguyenminhkhang.taskmanagement.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.database.entity.SortedType
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.AppMenuItem
import com.nguyenminhkhang.taskmanagement.ui.home.state.HomeUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TabUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskPageUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTabUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTaskUiState
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarActionType
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ID_ADD_NEW_LIST = -999L
const val ID_ADD_FAVORITE_LIST = -1000L

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepo: TaskRepo
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var _snackBarEvent = MutableSharedFlow<SnackbarEvent>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    private var pendingCompleteAction : Job? = null
    private var taskToConfirm : TaskUiState? = null

    private var _currentSelectedCollectionId:Long = -1L

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (taskRepo.getTaskCollection().first().isEmpty()) {
                taskRepo.addNewCollection("Personal")
                taskRepo.addNewCollection("Work")
            }

            val persistentListFlow: Flow<List<TaskGroupUiState>> = taskRepo.getTaskCollection()
                .flatMapLatest { collections ->
                    if (collections.isEmpty()) {
                        flowOf(emptyList())
                    } else {
                        val taskFlows = collections.map { collection ->
                            taskRepo.getAllTaskByCollectionId(collection.id!!)
                        }
                        combine(taskFlows) { arrayOfTaskLists ->
                            collections.mapIndexed { index, collection ->
                                val tasksForThisCollection = arrayOfTaskLists[index]
                                val taskUiStates = tasksForThisCollection.map { it.toTaskUiState() }

                                TaskGroupUiState(
                                    tab = collection.toTabUiState(),
                                    page = TaskPageUiState(
                                        activeTaskList = taskUiStates.filter { !it.isCompleted },
                                        completedTaskList = taskUiStates.filter { it.isCompleted }
                                    )
                                )
                            }
                        }
                    }
                }
                .map { taskGroupsFromDb ->
                    listOf(
                        TaskGroupUiState(
                            tab = TabUiState(ID_ADD_FAVORITE_LIST, "⭐️", sortedType = SortedType.SORTED_BY_DATE),
                            page = TaskPageUiState(
                                activeTaskList = taskGroupsFromDb.flatMap { it.page.activeTaskList }
                                    .filter { it.isFavorite }
                                    .sortedByDescending { it.updatedAt },
                                completedTaskList = emptyList()
                            )
                        )
                    ) + taskGroupsFromDb.map { tabItem ->
                        val sortedType = tabItem.tab.sortedType
                        val activeTaskList = tabItem.page.activeTaskList
                        tabItem.copy(
                            tab = tabItem.tab,
                            page = tabItem.page.copy(
                                activeTaskList = if (sortedType == SortedType.SORTED_BY_FAVORITE) {
                                    activeTaskList.sortedByDescending { it.isFavorite }
                                } else {
                                    activeTaskList.sortedByDescending { it.createdAt }
                                },
                                completedTaskList = tabItem.page.completedTaskList.sortedByDescending { it.updatedAt }
                            )
                        )
                    } + TaskGroupUiState( // Cuối cùng, thêm tab "+ New Tab"
                        tab = TabUiState(ID_ADD_NEW_LIST, "+ New Tab", sortedType = SortedType.SORTED_BY_DATE),
                        page = TaskPageUiState(
                            activeTaskList = emptyList(),
                            completedTaskList = emptyList()
                        )
                    )
                }
                .stateIn(
                    scope = viewModelScope,
                    initialValue = emptyList(),
                    started = SharingStarted.WhileSubscribed(5000L),
                )

            persistentListFlow.onEach { persistentList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        listTabGroup = persistentList,
                        isLoading = false
                    )
                }
            }
                .launchIn(viewModelScope)
        }
    }

    private fun addNewTask() {
        val state = _uiState.value
        if (state.newTaskContent.isBlank() || _currentSelectedCollectionId <= 0) return

        val content = state.newTaskContent
        val detail = state.newTaskDetail
        val isFavorite = state.newTaskIsFavorite
        val startDate = state.selectedDate
        val startTime = state.selectedTime

        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.addTask(
                content = content,
                collectionId = _currentSelectedCollectionId,
                taskDetail = detail,
                isFavorite = isFavorite,
                startDate = startDate,
                startTime = startTime
            )
        }
    }

    private fun handleToggleFavorite(task: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.updateTaskFavorite(taskId = task.id!!, isFavorite = !task.isFavorite)
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
                message = "Task '${task.content}' marked as ${if (task.isCompleted) "incomplete" else "complete"}",
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

        val restoredList = createOptimisticListForToggle(taskToRestore.copy(isCompleted = !taskToRestore.isCompleted))
        _uiState.update { it.copy(listTabGroup = restoredList) }

        taskToConfirm = null
    }

    private fun confirmToggleComplete() {
        val task = taskToConfirm ?: return

        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.updateTaskCompleted(task.id!!, !task.isCompleted)
        }
        taskToConfirm = null
    }

    private fun createOptimisticListForToggle(taskToChange: TaskUiState): List<TaskGroupUiState> {
        val currentList = _uiState.value.listTabGroup
        val isCompleting = !taskToChange.isCompleted

        return currentList.map { group->
            if(group.tab.id == _currentSelectedCollectionId) {
                val newActiveList = if(isCompleting) {
                    group.page.activeTaskList.filter { it.id != taskToChange.id }
                } else {
                    group.page.activeTaskList + taskToChange.copy(isCompleted = isCompleting)
                }

                val newCompletedList = if(isCompleting) {
                    group.page.completedTaskList + taskToChange.copy(isCompleted = isCompleting)
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
            taskRepo.deleteTaskCollectionById(collectionId)
        }
    }

    private fun requestAddNewCollection() {
        val currentName = _uiState.value.newTaskCollectionName.trim()
        if (currentName.isBlank()) return

        viewModelScope.launch {
            taskRepo.addNewCollection(currentName)
        }
    }

    private fun requestUpdateCollection(collectionId: Long) {
        val actionsList = listOf(
            AppMenuItem(title = "Delete Collection") {
                deleteCollectionById(collectionId) },
            AppMenuItem(title = "Rename Collection") {}
        )
        _uiState.update {
            it.copy(menuListButtonSheet = actionsList)
        }
    }

    private fun sortTaskCollection(collectionId: Long, sortedType: SortedType) {
        viewModelScope.launch {
            taskRepo.updateCollectionSortedType(collectionId, sortedType)
        }
    }

    private fun requestSortTasks(collectionId: Long) {
        val menuItems = listOf(
            AppMenuItem(title = "Sort by Date") {
                sortTaskCollection(collectionId, SortedType.SORTED_BY_DATE)
            },
            AppMenuItem(title = "Sort by Favorite") {
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

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ShowAddTaskSheet -> _uiState.update {
                if ( _currentSelectedCollectionId>0L ) {
                    it.copy(isAddTaskSheetVisible = true)
                } else it
            }
            is HomeEvent.HideAddTaskSheet -> _uiState.update { it.copy(isAddTaskSheetVisible = false) }
            is HomeEvent.TaskContentChanged -> _uiState.update { it.copy(newTaskContent = event.content) }
            is HomeEvent.TaskDetailChanged -> _uiState.update { it.copy(newTaskDetail = event.detail) }
            is HomeEvent.ToggleNewTaskFavorite -> _uiState.update { it.copy(newTaskIsFavorite = !it.newTaskIsFavorite) }
            is HomeEvent.ShowAddDetailTextField -> _uiState.update { it.copy(isShowAddDetailTextField = true) }
            is HomeEvent.ShowDatePicker -> _uiState.update { it.copy(isDatePickerVisible = true) }
            is HomeEvent.HideDatePicker -> _uiState.update { it.copy(isDatePickerVisible = false) }
            is HomeEvent.DateSelected -> _uiState.update { it.copy(selectedDate = event.date) }
            is HomeEvent.ShowTimePicker -> _uiState.update { it.copy(isTimePickerVisible = true) }
            is HomeEvent.HideTimePicker -> _uiState.update { it.copy(isTimePickerVisible = false) }
            is HomeEvent.TimeSelected -> _uiState.update { it.copy(selectedTime = event.time) }
            is HomeEvent.SelectedDateTimeCleared -> _uiState.update {
                it.copy(
                    selectedDate = null,
                    selectedTime = null
                )
            }
            is HomeEvent.SaveNewTask -> addNewTask()
            is HomeEvent.NewTaskCleared -> _uiState.update {
                it.copy(
                    newTaskContent = "",
                    newTaskDetail = "",
                    newTaskIsFavorite = false,
                    isAddTaskSheetVisible = false,
                    isShowAddDetailTextField = false,
                    isDatePickerVisible = false,
                    isTimePickerVisible = false,
                    selectedDate = null,
                    selectedTime = null
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
            is HomeEvent.AddNewCollectionRequested -> requestAddNewCollection()
            is HomeEvent.NewCollectionNameChanged -> NewCollectionNameChanged(event.name)
            is HomeEvent.NewCollectionNameCleared -> _uiState.update { it.copy(newTaskCollectionName = "") }
            is HomeEvent.RequestShowButtonSheetOption -> _uiState.update { it.copy(menuListButtonSheet = event.list) }
            is HomeEvent.UndoToggleComplete -> undoToggleComplete()
        }
    }
}