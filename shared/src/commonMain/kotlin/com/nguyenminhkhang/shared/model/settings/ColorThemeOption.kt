package com.nguyenminhkhang.shared.model.settings

enum class ColorThemeOption(val key: String) {
    PURPLE("purple"),
    RED("red"),
    GREEN("green"),
    BLUE("blue"),
    ORANGE("orange");

    companion object {
        fun fromStorage(value: String?): ColorThemeOption {
            if (value.isNullOrBlank()) return PURPLE
            return entries.firstOrNull { it.key == value }
                ?: entries.firstOrNull { it.name == value }
                ?: PURPLE
        }
    }
}