package com.nguyenminhkhang.taskmanagement.ui.home

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.database.entity.SortedType
import com.nguyenminhkhang.taskmanagement.handler.TaskCompletionHandler
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.AppMenuItem
import com.nguyenminhkhang.taskmanagement.ui.home.state.NewTaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TabUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskPageUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.millisToDateString
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTabUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTaskUiState
import com.nguyenminhkhang.taskmanagement.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ID_ADD_NEW_LIST = -999L
const val ID_ADD_FAVORITE_LIST = -1000L

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepo: TaskRepo,
    private val completionHandler: TaskCompletionHandler
) : ViewModel(), TaskDelegate {
    private val _eventFlow: MutableSharedFlow<MainEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(NewTaskUiState())
    val uiState: StateFlow<NewTaskUiState> = _uiState.asStateFlow()

    val persistentListGroup: StateFlow<List<TaskGroupUiState>> =
        taskRepo.getTaskCollection()
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

    private val _displayedListGroup = MutableStateFlow<List<TaskGroupUiState>>(emptyList())
    val listTabGroup: StateFlow<List<TaskGroupUiState>> = _displayedListGroup.asStateFlow()

    private var _currentSelectedCollectionId:Long = -1L

    private var _snackBarEvent = MutableSharedFlow<SnackbarEvent>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (taskRepo.getTaskCollection().first().isEmpty()) {
                taskRepo.addNewCollection("Personal")
                taskRepo.addNewCollection("Work")
            }

            persistentListGroup.collect { persistentState ->
                _displayedListGroup.value = persistentState
            }
        }
    }

    override fun addNewTask(collectionId: Long, content: String, taskDetail: String, isFavorite: Boolean , startDate: Long, startTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.addTask(content, collectionId, taskDetail, isFavorite, startDate, startTime)
        }
    }

    override fun invertTaskFavorite(taskUiState: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTaskUiState = taskUiState.copy(isFavorite = !taskUiState.isFavorite)
            taskRepo.updateTaskFavorite(newTaskUiState.id!!, newTaskUiState.isFavorite)
                .let { isSuccess ->
                if(!isSuccess) {
                    return@launch
                }
            }
        }
    }

    override fun invertTaskCompleted(taskUiState: TaskUiState) {
        completionHandler.invertTaskCompletion(
            scope = viewModelScope,
            task = taskUiState,
            onUpdateList = { updatedTask ->
                _updateUiWithTask(updatedTask)
            },
            onShowSnackbar = { event ->
                _snackBarEvent.emit(event)
            }
        )
    }

    fun handleTaskCompletionResult(taskId: Long) {
        val taskToComplete = _displayedListGroup.value.map { tabGroup ->
                tabGroup.page.activeTaskList.firstOrNull { task -> task.id == taskId }
                    ?: tabGroup.page.completedTaskList.firstOrNull { task -> task.id == taskId }
            }.firstOrNull { it != null
        }

        taskToComplete?.let {
            invertTaskCompleted(it)
        }?:Log.d("DEBUG_FLOW", "LỖI: Không tìm thấy task với ID = $taskId")
    }

    fun undoToggleComplete() {
        completionHandler.undo { originalTask ->
            _updateUiWithTask(originalTask)
        }
    }

    fun confirmToggleComplete() {
        completionHandler.confirm(viewModelScope)
    }

    private fun _updateUiWithTask(taskToUpdate: TaskUiState) {
        _displayedListGroup.value.let{ listTabGroup->
            val newListTabGroup = listTabGroup.map { tabGroup ->
                val sumList = tabGroup.page.completedTaskList + tabGroup.page.activeTaskList
                val updateList = sumList.map{task ->
                    if(task.id == taskToUpdate.id) {
                        val newUpdateAt = Calendar.getInstance().timeInMillis
                        taskToUpdate.copy(
                            updatedAt = newUpdateAt,
                            stringUpdateAt = newUpdateAt.millisToDateString()
                        )
                    } else task
                }
                val newPage = tabGroup.page.copy(
                    activeTaskList = updateList.filter{ !it.isCompleted },
                    completedTaskList = updateList.filter{ it.isCompleted}
                )
                tabGroup.copy(page = newPage)
            }
            _displayedListGroup.value = newListTabGroup
        }
    }

    override fun addNewTaskToCurrentCollection(content: String, taskDetail: String, isFavorite: Boolean, startDate: Long, startTime: Long) {
        viewModelScope.launch {
            persistentListGroup.value.firstOrNull { it.tab.id == _currentSelectedCollectionId }
                ?.let { currentTab ->
                val collectionId = currentTab.tab.id
                if(collectionId > 0) addNewTask(collectionId, content, taskDetail, isFavorite, startDate, startTime)
                else {
                    _eventFlow.emit(MainEvent.RequestAddNewCollection)
                }
            }
        }
    }

    override fun updateCurrentCollectionId(collectionId: Long) {
        _currentSelectedCollectionId = collectionId
    }

    override fun currentCollectionId(): Long {
        return _currentSelectedCollectionId
    }

    override fun addNewCollection(content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.addNewCollection(content)
        }
    }

    override fun requestAddNewCollection() {
        viewModelScope.launch {
            _eventFlow.emit(MainEvent.RequestAddNewCollection)
        }
    }

    override fun requestUpdateCollection(collectionId: Long) {
        val actionsList = listOf(
            AppMenuItem(title = "Delete Collection") {
                deleteCollectionById(collectionId)
            },
            AppMenuItem(title = "Rename Collection") {
            }
        )
        viewModelScope.launch {
            _eventFlow.emit(MainEvent.RequestShowButtonSheetOption(actionsList))
        }
    }

    fun deleteCollectionById(collectionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.deleteTaskCollectionById(collectionId)
        }
    }

    private fun sortTaskCollection(collectionId: Long, sortedType: SortedType) {
        viewModelScope.launch {
            taskRepo.updateCollectionSortedType(collectionId, sortedType)
        }
    }

    override fun requestSortTasks(collectionId: Long) {
        viewModelScope.launch {
            _eventFlow.emit(MainEvent.RequestShowButtonSheetOption(
                listOf(
                    AppMenuItem(title = "Sort by Favorite") {
                        sortTaskCollection(collectionId, sortedType = SortedType.SORTED_BY_FAVORITE)
                    },
                    AppMenuItem(title = "Sort by Date") {
                        sortTaskCollection(collectionId, sortedType = SortedType.SORTED_BY_DATE)
                    }
                )
            ))
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ShowAddTaskSheet -> _uiState.update { it.copy(isAddTaskSheetVisible = true) }
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
            is HomeEvent.ClearSelectedDateTime -> _uiState.update {
                it.copy(
                    selectedDate = null,
                    selectedTime = null
                )
            }
            is HomeEvent.SaveNewTask -> {
                if (_uiState.value.newTaskContent.isNotBlank()) {
                    viewModelScope.launch {
                        taskRepo.addTask(
                            content = _uiState.value.newTaskContent,
                            collectionId = _currentSelectedCollectionId,
                            taskDetail = _uiState.value.newTaskDetail,
                            isFavorite = _uiState.value.newTaskIsFavorite,
                            startDate = _uiState.value.selectedDate,
                            startTime = _uiState.value.selectedTime
                        )
                    }
                }
            }
            is HomeEvent.ClearNewTask -> _uiState.update {
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
        }
    }
}

sealed class MainEvent {
    data object RequestAddNewCollection : MainEvent()
    data class RequestShowButtonSheetOption(val list: List<AppMenuItem>) : MainEvent()
}