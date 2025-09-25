package com.nguyenminhkhang.taskmanagement.datastore

import com.nguyenminhkhang.taskmanagement.R
import java.util.Locale

// Hàm này trả về StringRes tương ứng với ngôn ngữ hiện tại của hệ thống
fun getSystemLanguageResId(): Int {
    val systemLanguageCode = Locale.getDefault().language
    return when (systemLanguageCode) {
        "en" -> R.string.language_english
        "vi" -> R.string.language_vietnamese
        else -> R.string.language_english // Mặc định là Tiếng Anh nếu không khớp
    }
}