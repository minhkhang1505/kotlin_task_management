package com.nguyenminhkhang.taskmanagement

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.AppMenuItem
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TabUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskPageUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.millisToDateString
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTabUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("MainViewModel", "Init block started")

                val taskCollections = taskRepo.getTaskCollection()
                Log.d("MainViewModel", "Fetched ${taskCollections.size} task collections")

                if (taskCollections.isEmpty()) {
                    taskRepo.addNewCollection("Personal")
                    taskRepo.addNewCollection("Work")
                }

                val updatedTaskCollections = taskRepo.getTaskCollection()

                val listTabGroupUiState = updatedTaskCollections.map { taskCollection ->
                    val tasks = taskRepo.getAllTaskByCollectionId(taskCollection.id!!)

                    val taskUiStates = tasks.map { taskEntity ->
                        taskEntity.toTaskUiState()
                    }

                    TaskGroupUiState(
                        tab = taskCollection.toTabUiState(),
                        page = TaskPageUiState(
                            activeTaskList = taskUiStates.filter{task -> !task.isCompleted},
                            completedTaskList = taskUiStates.filter{task -> task.isCompleted}
                        )
                    )
                }
                _listTabGroup.value = listTabGroupUiState
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error during init: ${e.message}", e)
            }
        }
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

    override fun requestUpdateCollection(collectionId: Long) {
        Log.d("MainViewModel", "requestUpdateCollection: $collectionId")
        val actionsList = listOf(
            AppMenuItem(title = "Delete Collection") {
                deleteCollectionById(collectionId)
                Log.d("MainViewModel", "requestUpdateCollection: Delete Collection $collectionId")
            },
            AppMenuItem(title = "Rename Collection") {
                Log.d("MainViewModel", "requestUpdateCollection: Rename Collection $collectionId")
            }
        )
        viewModelScope.launch {
            _eventFlow.emit(MainEvent.RequestShowButtonSheetOption(actionsList))
        }
    }

    fun deleteCollectionById(collectionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            if(taskRepo.deleteTaskCollectionById(collectionId)) {
                _listTabGroup.value.let { listTabs ->
                    val newTabGroup = listTabs.filter { tabItem -> tabItem.tab.id != collectionId }
                    _listTabGroup.value = newTabGroup
                }
            }
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
}

sealed class MainEvent {
    data object RequestAddNewCollection : MainEvent()
    data class RequestShowButtonSheetOption(val list: List<AppMenuItem>) : MainEvent()
}