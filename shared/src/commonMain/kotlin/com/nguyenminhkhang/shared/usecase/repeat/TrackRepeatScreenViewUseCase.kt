package com.nguyenminhkhang.shared.usecase.repeat

import com.nguyenminhkhang.shared.analytics.AnalyticsEvent
import com.nguyenminhkhang.shared.analytics.AnalyticsTracker


class TrackRepeatScreenViewUseCase (
    private val analyticsTracker: AnalyticsTracker
) {
    operator fun invoke() {
        analyticsTracker.trackEvent(AnalyticsEvent.ScreenView("RepeatScreen"))
    }

    companion object {
        private const val TAG = "TrackRepeatScreenViewUseCase"
    }
}
