package com.nguyenminhkhang.taskmanagement.core.analytics

interface AnalyticsTracker {
    fun trackEvent(event: AnalyticsEvent)
}