package com.nguyenminhkhang.taskmanagement.domain.usecase.taskdetail

import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsEvent
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsTracker
import javax.inject.Inject

class TrackTaskDetailScreenViewUseCase @Inject constructor(
    private val analyticsTracker: AnalyticsTracker
) {
    operator fun invoke() {
        analyticsTracker.trackEvent(AnalyticsEvent.ScreenView("TaskDetailScreen"))
    }
}