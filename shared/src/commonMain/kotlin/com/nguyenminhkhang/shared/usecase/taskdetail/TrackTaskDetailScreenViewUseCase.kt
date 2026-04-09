package com.nguyenminhkhang.shared.usecase.taskdetail

import com.nguyenminhkhang.shared.analytics.AnalyticsEvent
import com.nguyenminhkhang.shared.analytics.AnalyticsTracker


class TrackTaskDetailScreenViewUseCase (
    private val analyticsTracker: AnalyticsTracker
) {
    operator fun invoke() {
        analyticsTracker.trackEvent(AnalyticsEvent.ScreenView("TaskDetailScreen"))
    }
}