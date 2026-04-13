package com.nguyenminhkhang.shared.model.settings

enum class FontStyleOption(val key: String) {
    DEFAULT("default"),
    SERIF("serif"),
    SANS_SERIF("sans_serif"),
    MONOSPACE("monospace"),
    CURSIVE("cursive");

    companion object {
        fun fromStorage(value: String?): FontStyleOption {
            if (value.isNullOrBlank()) return DEFAULT
            return entries.firstOrNull { it.key == value }
                ?: entries.firstOrNull { it.name == value }
                ?: DEFAULT
        }
    }
}