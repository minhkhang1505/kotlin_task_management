package com.nguyenminhkhang.shared.analytics

interface AnalyticsTracker {
    fun trackEvent(event: AnalyticsEvent)
}