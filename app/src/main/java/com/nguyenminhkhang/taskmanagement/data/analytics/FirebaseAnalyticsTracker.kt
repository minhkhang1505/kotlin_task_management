package com.nguyenminhkhang.taskmanagement.data.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsEvent
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsTracker
import jakarta.inject.Inject

class FirebaseAnalyticsTracker @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsTracker {
    override fun trackEvent(event: AnalyticsEvent) {
        val bundle = Bundle()

        when(event) {
            is AnalyticsEvent.AddTask -> {
                bundle.putLong("task_id", event.taskId)
            }
            is AnalyticsEvent.DeleteTask -> {
                bundle.putLong("task_id", event.taskId)
            }
            is AnalyticsEvent.UpdateTask -> {
                bundle.putLong("task_id", event.taskId)
            }
            is AnalyticsEvent.CompleteTask -> {
                bundle.putLong("task_id", event.taskId)
            }
            is AnalyticsEvent.CreateCollection -> {
                bundle.putString("name", event.newCollectionName)
            }
            is AnalyticsEvent.DeleteCollection -> {
                bundle.putLong("collection_id", event.collectionId)
            }
            is AnalyticsEvent.RenameCollection -> {
                bundle.putLong("collection_id", event.collectionId)
            }
            is AnalyticsEvent.OpenApp -> {}
            is AnalyticsEvent.ScreenView -> {}
        }

        firebaseAnalytics.logEvent(event.name, bundle)
    }
}