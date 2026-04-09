package com.nguyenminhkhang.shared.usecase

import com.nguyenminhkhang.shared.model.SortedType
import com.nguyenminhkhang.shared.model.TaskGroup
import com.nguyenminhkhang.shared.model.TaskPage
import com.nguyenminhkhang.shared.repository.TaskRepository
import com.nguyenminhkhang.shared.usecase.collectionusecase.GetTaskCollectionsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetTaskGroupsUseCase(
    private val getTaskCollectionsUseCase: GetTaskCollectionsUseCase,
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<TaskGroup>> {
        return getTaskCollectionsUseCase.invoke()
            .flatMapLatest { collections ->
                val validCollections = collections.filter { it.id != null }
                if (validCollections.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    val taskFlows = validCollections.map { collection ->
                        taskRepository.getAllTaskByCollectionId(requireNotNull(collection.id))
                    }

                    combine(taskFlows) { arrayOfTaskLists ->
                        validCollections.mapIndexed { index, collection ->
                            val tasksForCollection = arrayOfTaskLists[index]
                            TaskGroup(
                                collection = collection,
                                page = TaskPage(
                                    activeTaskList = tasksForCollection
                                        .filter { !it.completed }
                                        .let { tasks ->
                                            if (collection.sortedType == SortedType.SORTED_BY_FAVORITE) {
                                                tasks.sortedByDescending { it.favorite }
                                            } else {
                                                tasks.sortedByDescending { it.createdAt }
                                            }
                                        },
                                    completedTaskList = tasksForCollection
                                        .filter { it.completed }
                                        .sortedByDescending { it.updatedAt }
                                )
                            )
                        }
                    }
                }
            }
            .map { groups ->
                groups.sortedByDescending { it.collection.updatedAt }
            }
    }
}