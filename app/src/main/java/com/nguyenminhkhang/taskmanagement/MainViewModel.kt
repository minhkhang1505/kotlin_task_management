package com.nguyenminhkhang.taskmanagement

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.database.entity.SortedType
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.handler.TaskCompletionHandler
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.AppMenuItem
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ID_ADD_NEW_LIST = -999L
const val ID_ADD_FAVORITE_LIST = -1000L

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepo: TaskRepo,
    private val completionHandler: TaskCompletionHandler
) : ViewModel(), TaskDelegate {
    private val _eventFlow: MutableSharedFlow<MainEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()

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
            // Lấy giá trị đầu tiên để kiểm tra, first() sẽ tự hủy coroutine
            if (taskRepo.getTaskCollection().first().isEmpty()) {
                taskRepo.addNewCollection("Personal")
                taskRepo.addNewCollection("Work")
            }

            persistentListGroup.collect { persistentState ->
                _displayedListGroup.value = persistentState
            }
        }
    }

    override fun addNewTask(collectionId: Long, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.addTask(content, collectionId)
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
                Log.d("DEBUG_FLOW", "5. VIEWMODEL PHÁT SỰ KIỆN: Chuẩn bị phát Snackbar: ${event.message}")
                _snackBarEvent.emit(event)
            }
        )
    }

    fun handleTaskCompletionResult(taskId: Long) {
        Log.d("INSTANCE_CHECK", "ViewModel đang XỬ LÝ KẾT QUẢ có HashCode: ${this.hashCode()}")
        Log.d("DEBUG_FLOW", "4. VIEWMODEL NHẬN LỆNH: Đang xử lý cho task ID = $taskId")
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
        Log.d("DEBUG_FLOW", "7. VIEWMODEL Hoàn Tất: Da luu thay doi trong CSDL")
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

    override fun addNewTaskToCurrentCollection(content: String) {
        viewModelScope.launch {
            persistentListGroup.value.firstOrNull { it.tab.id == _currentSelectedCollectionId }
                ?.let { currentTab ->
                val collectionId = currentTab.tab.id
                if(collectionId > 0) addNewTask(collectionId, content)
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
}

interface TaskDelegate {
    fun invertTaskFavorite(taskUiState: TaskUiState) = Unit
    fun invertTaskCompleted(taskUiState: TaskUiState) = Unit
    fun addNewTask(collectionId: Long, content: String) = Unit
    fun addNewTaskToCurrentCollection(content: String) = Unit
    fun updateCurrentCollectionId(collectionId: Long) = Unit
    fun currentCollectionId(): Long = -1L
    fun addNewCollection(content: String) = Unit
    fun requestAddNewCollection() = Unit
    fun requestUpdateCollection(collectionId: Long) = Unit
    fun requestSortTasks(collectionId: Long) = Unit
}

sealed class MainEvent {
    data object RequestAddNewCollection : MainEvent()
    data class RequestShowButtonSheetOption(val list: List<AppMenuItem>) : MainEvent()
}