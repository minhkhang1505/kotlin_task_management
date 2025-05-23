package com.nguyenminhkhang.taskmanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TabUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskPageUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepo: TaskRepo
) : ViewModel(), TaskDelegate {
    private val _listTabGroup: MutableStateFlow<List<TaskGroupUiState>> = MutableStateFlow(emptyList())
    val listTabGroup = _listTabGroup.asStateFlow()

    private var _currentCollectedCollectionIndex:Int = 0

    init {
        _listTabGroup.value = listOf(
            TaskGroupUiState(
                tab = TabUiState(1,"Tab 1"),
                page = TaskPageUiState(
                    listOf(
                        TaskUiState(
                            id = 1,
                            content = "Task 1",
                            collectionId = 1,
                            updatedAt = 1232
                        ),
                        TaskUiState(
                            id = 2,
                            content = "Task 2",
                            collectionId = 1,
                            updatedAt = 1233
                        ),
                        TaskUiState(
                            id = 3,
                            content = "Task 3",
                            collectionId = 1,
                            isFavorite = true,
                            updatedAt = 1234
                        ),
                    ), listOf()
                )
            ),
            TaskGroupUiState(
                tab = TabUiState(1,"Tab 1"),
                page = TaskPageUiState(
                    listOf(), listOf()
                )
            )
        )
    }

    override fun invertTaskFavorite(taskUiState: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTaskUiState = taskUiState.copy(isFavorite = !taskUiState.isFavorite)
            listTabGroup.value.let{listTabGroup->
                val newListTabGroup = listTabGroup.map { tabGroup ->
                    val newPage = tabGroup.page.copy(
                        activeTaskList = tabGroup.page.activeTaskList.map {task ->
                            if(task.id == newTaskUiState.id) newTaskUiState.copy() else {task}
                        },
                        completedTaskList = tabGroup.page.completedTaskList.map {task ->
                            if(task.id == newTaskUiState.id) newTaskUiState.copy() else task
                        }
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
            listTabGroup.value.let{listTabGroup->
                val newListTabGroup = listTabGroup.map { tabGroup ->
                    val sumList = tabGroup.page.completedTaskList + tabGroup.page.activeTaskList
                    val updateList = sumList.map{task ->
                        if(task.id == newTaskUiState.id) newTaskUiState.copy() else task
                    }
                    val newPage = tabGroup.page.copy(
                        activeTaskList = updateList.filter{ !it.isCompleted },
                        completedTaskList = updateList.filter{ it.isCompleted}
                    )
                    tabGroup.copy(page = newPage)
                }
                _listTabGroup.value = newListTabGroup
            }
        }
    }

    override fun addnewTask(collectionId: Long, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.addTask(content, collectionId)?.let {taskEntity ->
                val newTaskUiState = taskEntity.toTaskUiState()
                listTabGroup.value.let {listTabGroup ->
                    val newTabGroup = listTabGroup.map { tabGroup ->
                        val newPage = tabGroup.page.copy(
                            activeTaskList = tabGroup.page.activeTaskList + newTaskUiState,
                        )
                        tabGroup.copy(page = newPage)
                    }
                    _listTabGroup.value = newTabGroup
                }
            }
        }
    }

    override fun addNewTaskToCurrentCollection(content: String) {
        viewModelScope.launch {
            val currentTab = listTabGroup.value.getOrNull(_currentCollectedCollectionIndex)?.let {
                currentTab ->
                val collectionId = currentTab.tab.id
                addnewTask(collectionId, content)
            }
        }
    }

    override fun updateCurrentCollectionIndex(index: Int) {
        _currentCollectedCollectionIndex = index
    }
}

interface TaskDelegate {
    fun invertTaskFavorite(taskUiState: TaskUiState) : Unit
    fun invertTaskCompleted(taskUiState: TaskUiState) : Unit
    fun addnewTask(collectionId: Long, content: String) : Unit
    fun addNewTaskToCurrentCollection(content: String) : Unit
    fun updateCurrentCollectionIndex(index: Int) : Unit
}