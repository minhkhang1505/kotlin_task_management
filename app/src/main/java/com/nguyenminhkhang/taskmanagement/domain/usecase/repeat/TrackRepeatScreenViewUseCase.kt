package com.nguyenminhkhang.taskmanagement.domain.usecase.repeat

import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsEvent
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsTracker
import timber.log.Timber
import javax.inject.Inject

class TrackRepeatScreenViewUseCase (
    private val analyticsTracker: AnalyticsTracker
) {
    operator fun invoke() {
        Timber.tag(TAG).d("invoke() - Tracking RepeatScreen view event")
        analyticsTracker.trackEvent(AnalyticsEvent.ScreenView("RepeatScreen"))
    }

    companion object {
        private const val TAG = "TrackRepeatScreenViewUseCase"
    }
}
