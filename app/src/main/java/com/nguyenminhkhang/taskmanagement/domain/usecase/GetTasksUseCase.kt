package com.nguyenminhkhang.taskmanagement.domain.usecase

import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

enum class TaskQueryType {
    BY_COLLECTION_ID,
    TODAY,
    SEARCH
}

data class GetTasksParams(
    val queryType: TaskQueryType,
    val collectionId: Long? = null,
    val searchQuery: String? = null,
    val todayStartDay: Long? = null,
    val todayEndDay: Long? = null
)

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(params: GetTasksParams) : Flow<List<TaskEntity>> {
        return when(params.queryType) {
            TaskQueryType.BY_COLLECTION_ID -> {
                requireNotNull(params.collectionId) {
                    "Collection ID must not be null when query type is BY_COLLECTION_ID"
                }
                taskRepository.getAllTaskByCollectionId(params.collectionId)
            }
            TaskQueryType.SEARCH -> {
                requireNotNull(params.searchQuery) {
                    "Search query must not be null when query type is SEARCH"
                }
                taskRepository.SearchTasks(params.searchQuery)
            }
            TaskQueryType.TODAY -> {
                requireNotNull(params.todayStartDay) {
                    "Today start day must not be null when query type is TODAY"
                }
                requireNotNull(params.todayEndDay) {
                    "Today end day must not be null when query type is TODAY"
                }
                taskRepository.getTodayTasks(params.todayStartDay, params.todayEndDay)
            }
        }
    }
}