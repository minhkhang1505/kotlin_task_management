package com.nguyenminhkhang.taskmanagement.data.datastore

import com.nguyenminhkhang.shared.model.settings.LanguageOption
import java.util.Locale

// Return StringRes matching with current system Language
fun getSystemLanguageResId(): String {
    val systemLanguageCode = Locale.getDefault().language
    return when (systemLanguageCode) {
        "en" -> LanguageOption.ENGLISH.code
        "vi" -> LanguageOption.VIETNAMESE.code
        else -> LanguageOption.ENGLISH.code // Default English if not corrent
    }
}