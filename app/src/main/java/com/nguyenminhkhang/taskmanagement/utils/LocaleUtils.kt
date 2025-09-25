package com.nguyenminhkhang.taskmanagement.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun Context.setLocale(languageCode: String): Context {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration(resources.configuration)
    config.setLocale(locale)

    return createConfigurationContext(config)
}