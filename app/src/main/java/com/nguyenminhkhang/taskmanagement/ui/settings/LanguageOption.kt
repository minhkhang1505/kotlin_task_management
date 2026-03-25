package com.nguyenminhkhang.taskmanagement.ui.settings

enum class LanguageOption(val code: String) {
    ENGLISH("en"),
    VIETNAMESE("vi");

    companion object {
        fun fromStorage(value: String?): LanguageOption {
            if (value.isNullOrBlank()) return ENGLISH

            return entries.firstOrNull { it.code == value }
                ?: entries.firstOrNull { it.name == value }
                ?: ENGLISH
        }
    }
}