package com.nguyenminhkhang.taskmanagement.ui.home

import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskUiState

interface TaskDelegate {
    fun invertTaskFavorite(taskUiState: TaskUiState) = Unit
    fun invertTaskCompleted(taskUiState: TaskUiState) = Unit
    fun addNewTask(collectionId: Long, content: String, taskDetail: String, isFavorite: Boolean , startDate: Long, startTime: Long) = Unit
    fun addNewTaskToCurrentCollection(content: String, taskDetail: String, isFavorite: Boolean, startDate: Long, startTime: Long) = Unit
    fun updateCurrentCollectionId(collectionId: Long) = Unit
    fun currentCollectionId(): Long = -1L
    fun addNewCollection(content: String) = Unit
    fun requestAddNewCollection() = Unit
    fun requestUpdateCollection(collectionId: Long) = Unit
    fun requestSortTasks(collectionId: Long) = Unit
}