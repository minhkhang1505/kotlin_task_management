package com.nguyenminhkhang.taskmanagement.domain.usecase

import com.nguyenminhkhang.taskmanagement.domain.model.SortedType
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.GetTaskCollectionsUseCase
import com.nguyenminhkhang.taskmanagement.ui.home.ID_ADD_FAVORITE_LIST
import com.nguyenminhkhang.taskmanagement.ui.home.ID_ADD_NEW_LIST
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TabUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskPageUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTabUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toTaskUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.plus

class GetTaskGroupsUseCase @Inject constructor(
    private val getTaskCollectionsUseCase: GetTaskCollectionsUseCase,
    private val taskRepository: TaskRepository
) {
    operator fun invoke(favoriteTabName: String, newTabName: String) : Flow<List<TaskGroupUiState>> {
        return getTaskCollectionsUseCase.invoke()
            .flatMapLatest { collections ->
                if (collections.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    val taskFlows = collections.map { collection ->
                        taskRepository.getAllTaskByCollectionId(collection.id!!)
                    }
                    combine(taskFlows) { arrayOfTaskLists ->
                        collections.mapIndexed { index, collection ->
                            val tasksForThisCollection = arrayOfTaskLists[index]
                            val taskUiStates = tasksForThisCollection.map { it.toTaskUiState() }

                            TaskGroupUiState(
                                tab = collection.toTabUiState(),
                                page = TaskPageUiState(
                                    activeTaskList = taskUiStates.filter { !it.completed },
                                    completedTaskList = taskUiStates.filter { it.completed }
                                )
                            )
                        }
                    }
                }
            }
            .map { taskGroupsFromDb ->
                listOf(
                    TaskGroupUiState(
                        tab = TabUiState(
                            ID_ADD_FAVORITE_LIST,
                            "⭐️",
                            sortedType = SortedType.SORTED_BY_DATE
                        ),
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
                    tab = TabUiState(
                        ID_ADD_NEW_LIST,
                        newTabName,
                        sortedType = SortedType.SORTED_BY_DATE
                    ),
                    page = TaskPageUiState(
                        activeTaskList = emptyList(),
                        completedTaskList = emptyList()
                    )
                )
            }
    }
}