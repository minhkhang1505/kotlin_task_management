package com.nguyenminhkhang.shared.analytics

sealed class AnalyticsEvent(val name: String) {
    object OpenApp : AnalyticsEvent("open_app")

    data class AddTask(val taskId: Long) : AnalyticsEvent("add_task")
    data class DeleteTask(val taskId: Long) : AnalyticsEvent("delete_task")
    data class UpdateTask(val taskId: Long) : AnalyticsEvent("update_task")
    data class CompleteTask(val taskId: Long) : AnalyticsEvent("complete_task")

    data class CreateCollection(val newCollectionName: String) : AnalyticsEvent("create_collection")
    data class RenameCollection(val collectionId: Long) : AnalyticsEvent("rename_collection")
    data class DeleteCollection(val collectionId: Long) : AnalyticsEvent("delete_collection")

    data class ScreenView(val screen: String) : AnalyticsEvent("screen_view")
}