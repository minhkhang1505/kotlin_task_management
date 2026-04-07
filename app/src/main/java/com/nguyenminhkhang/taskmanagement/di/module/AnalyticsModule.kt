package com.nguyenminhkhang.taskmanagement.di.module

import com.google.firebase.analytics.FirebaseAnalytics
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsTracker
import com.nguyenminhkhang.taskmanagement.data.analytics.FirebaseAnalyticsTracker
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val analyticsModule = module {
	single<FirebaseAnalytics> {
		FirebaseAnalytics.getInstance(androidContext())
	}

	single<AnalyticsTracker> { FirebaseAnalyticsTracker(get()) }
}