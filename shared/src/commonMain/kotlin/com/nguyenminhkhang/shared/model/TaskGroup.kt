package com.nguyenminhkhang.shared.model

data class TaskPage(
    val activeTaskList: List<Task>,
    val completedTaskList: List<Task>
)

data class TaskGroup(
    val collection: Collection,
    val page: TaskPage
)