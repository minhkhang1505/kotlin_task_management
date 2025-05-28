package com.nguyenminhkhang.taskmanagement

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TabUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskPageUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.millisToDateString
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTabUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

const val ID_ADD_NEW_LIST = -999L
const val ID_ADD_FAVORITE_LIST = -1000L

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepo: TaskRepo
) : ViewModel(), TaskDelegate {
    private val _eventFlow: MutableSharedFlow<MainEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _listTabGroup: MutableStateFlow<List<TaskGroupUiState>> = MutableStateFlow(emptyList())
    val listTabGroup = _listTabGroup.map {
        listOf(
            TaskGroupUiState(
                tab = TabUiState(ID_ADD_FAVORITE_LIST, "⭐️"),
                page = TaskPageUiState(
                    mutableListOf<TaskUiState>().apply {
                        it.forEach { tab ->
                            addAll(tab.page.activeTaskList.filter { task -> task.isFavorite })
                        }
                    }.sortedByDescending { it.updatedAt }, emptyList()
                )
            )
        ) + it + TaskGroupUiState(
            tab = TabUiState(ID_ADD_NEW_LIST, "Add New Tab"),
            page = TaskPageUiState(
                activeTaskList = emptyList(),
                completedTaskList = emptyList()
            )
        )
    }

    private var _currentSelectedCollectionId:Long = -1L

    init {
        _listTabGroup.value = listOf(
            TaskGroupUiState(
                tab = TabUiState(1, "Tab 1"),
                page = TaskPageUiState(
                    listOf(
                        TaskUiState(
                            id = 1,
                            content = "Task 1",
                            collectionId = 1,
                            updatedAt = 1232,
                            stringUpdateAt = "Thu, 01 Jan 1970" // Chỉnh sửa để có giá trị hợp lệ
                        ),
                        TaskUiState(
                            id = 2,
                            content = "Task 2",
                            collectionId = 1,
                            updatedAt = 1233,
                            stringUpdateAt = "Thu, 02 Jan 1970" // Chỉnh sửa để có giá trị hợp lệ
                        ),
                        TaskUiState(
                            id = 3,
                            content = "Task 3",
                            collectionId = 1,
                            isFavorite = true,
                            updatedAt = 1234,
                            stringUpdateAt = "Thu, 03 Jan 1970" // Chỉnh sửa để có giá trị hợp lệ
                        ),
                    ), listOf()
                )
            ),
            TaskGroupUiState(
                tab = TabUiState(2, "Tab 2"), // Sửa id thành 2, tên tab cũng đổi để dễ phân biệt
                page = TaskPageUiState(
                    listOf(), listOf()
                )
            )
        )
    }


    override fun invertTaskFavorite(taskUiState: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTaskUiState = taskUiState.copy(isFavorite = !taskUiState.isFavorite)
            taskRepo.updateTaskFavorite(newTaskUiState.id!!, newTaskUiState.isFavorite).let { isSuccess ->
                if(!isSuccess) {
                    Log.e("MainViewModel", "invertTaskFavorite: Failed to update task favorite status")
                    return@launch
                }
            }
            _listTabGroup.value.let{listTabGroup->
                val newListTabGroup = listTabGroup.map { tabGroup ->
                    val newPage = tabGroup.page.copy(
                        activeTaskList = tabGroup.page.activeTaskList.map {task ->
                            if(task.id == newTaskUiState.id) newTaskUiState.copy(
                                updatedAt = Calendar.getInstance().timeInMillis,
                                stringUpdateAt = Calendar.getInstance().time.toString()
                            ) else {task}
                        }.sortedByDescending { it.updatedAt },
                        completedTaskList = tabGroup.page.completedTaskList.map {task ->
                            if(task.id == newTaskUiState.id) newTaskUiState.copy(
                                updatedAt = Calendar.getInstance().timeInMillis,
                                stringUpdateAt = Calendar.getInstance().time.toString()
                            ) else task
                        }.sortedByDescending { it.updatedAt }
                    )
                    tabGroup.copy(page = newPage)
                }
                _listTabGroup.value = newListTabGroup
            }
        }
    }

    override fun invertTaskCompleted(taskUiState: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTaskUiState = taskUiState.copy(isCompleted = !taskUiState.isCompleted)
            if(!taskRepo.updateTaskCompleted(newTaskUiState.id!!, newTaskUiState.isCompleted)) {
                Log.e("MainViewModel", "invertTaskCompleted: Failed to update task completed status")
                return@launch
            }
            _listTabGroup.value.let{listTabGroup->
                val newListTabGroup = listTabGroup.map { tabGroup ->
                    val sumList = tabGroup.page.completedTaskList + tabGroup.page.activeTaskList
                    val updateList = sumList.map{task ->
                        if(task.id == newTaskUiState.id) {
                            val newUpdateAt = Calendar.getInstance().timeInMillis
                            newTaskUiState.copy(
                                updatedAt = newUpdateAt,
                                stringUpdateAt = newUpdateAt.millisToDateString()
                            )
                        } else task
                    }
                    val newPage = tabGroup.page.copy(
                        activeTaskList = updateList.filter{ !it.isCompleted }.sortedByDescending { it.updatedAt },
                        completedTaskList = updateList.filter{ it.isCompleted}.sortedByDescending { it.updatedAt }
                    )
                    tabGroup.copy(page = newPage)
                }
                _listTabGroup.value = newListTabGroup
            }
        }
    }

    override fun addNewTask(collectionId: Long, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.addTask(content, collectionId)?.let { taskEntity ->
                val newTaskUiState = taskEntity.toTaskUiState()
                val newTabGroup = _listTabGroup.value.map { tabGroup ->
                    if (tabGroup.tab.id == collectionId) {
                        val newPage = tabGroup.page.copy(
                            activeTaskList = tabGroup.page.activeTaskList + newTaskUiState,
                        )
                        tabGroup.copy(page = newPage)
                    } else {
                        tabGroup // giữ nguyên tabGroup không liên quan
                    }
                }
                _listTabGroup.value = newTabGroup
            }
        }
    }


    override fun addNewTaskToCurrentCollection(content: String) {
        viewModelScope.launch {
            _listTabGroup.value.firstOrNull { it.tab.id == _currentSelectedCollectionId }?.let { currentTab ->
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
            taskRepo.addNewCollection(content)?.let{ taskCollection ->
                val tabUiState = taskCollection.toTabUiState()
                val newTabGroup = TaskGroupUiState(tabUiState, TaskPageUiState(emptyList(), emptyList()))
                Log.d("PagerTabLayout", " in viewmodel addNewCollection: ${newTabGroup.tab.id}")
                _listTabGroup.value += newTabGroup
            }
        }
    }

    override fun requestAddNewCollection() {
        viewModelScope.launch {
            _eventFlow.emit(MainEvent.RequestAddNewCollection)
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
}

sealed class MainEvent {
    data object RequestAddNewCollection : MainEvent()
}