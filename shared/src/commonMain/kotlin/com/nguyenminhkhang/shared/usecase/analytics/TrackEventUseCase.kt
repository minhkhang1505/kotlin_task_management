package com.nguyenminhkhang.shared.usecase.analytics

import com.nguyenminhkhang.shared.analytics.AnalyticsEvent
import com.nguyenminhkhang.shared.analytics.AnalyticsTracker

class TrackEventUseCase(
	private val analyticsTracker: AnalyticsTracker
) {
	operator fun invoke(event: AnalyticsEvent) {
		analyticsTracker.trackEvent(event)
	}
}