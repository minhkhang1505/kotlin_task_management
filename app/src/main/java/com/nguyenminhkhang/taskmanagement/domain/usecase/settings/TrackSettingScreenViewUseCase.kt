package com.nguyenminhkhang.taskmanagement.domain.usecase.settings

import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsEvent
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsTracker
import javax.inject.Inject

class TrackSettingScreenViewUseCase @Inject constructor(
    private val analyticsTracker: AnalyticsTracker
) {
    operator fun invoke() {
        analyticsTracker.trackEvent(AnalyticsEvent.ScreenView("SettingScreen"))
    }
}