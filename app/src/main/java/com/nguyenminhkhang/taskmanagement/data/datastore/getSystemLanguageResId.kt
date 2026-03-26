package com.nguyenminhkhang.taskmanagement.data.datastore

import com.nguyenminhkhang.taskmanagement.ui.settings.LanguageOption
import java.util.Locale

// Hàm này trả về StringRes tương ứng với ngôn ngữ hiện tại của hệ thống
fun getSystemLanguageResId(): String {
    val systemLanguageCode = Locale.getDefault().language
    return when (systemLanguageCode) {
        "en" -> LanguageOption.ENGLISH.code
        "vi" -> LanguageOption.VIETNAMESE.code
        else -> LanguageOption.ENGLISH.code // Mặc định là Tiếng Anh nếu không khớp
    }
}