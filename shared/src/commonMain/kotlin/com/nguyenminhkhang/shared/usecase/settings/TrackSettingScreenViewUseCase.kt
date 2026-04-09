package com.nguyenminhkhang.shared.usecase.settings

import com.nguyenminhkhang.shared.analytics.AnalyticsEvent
import com.nguyenminhkhang.shared.analytics.AnalyticsTracker


class TrackSettingScreenViewUseCase (
    private val analyticsTracker: AnalyticsTracker
) {
    operator fun invoke() {
        analyticsTracker.trackEvent(AnalyticsEvent.ScreenView("SettingScreen"))
    }
}