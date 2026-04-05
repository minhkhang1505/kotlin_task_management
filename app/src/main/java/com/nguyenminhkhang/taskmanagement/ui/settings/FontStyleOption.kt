package com.nguyenminhkhang.taskmanagement.ui.settings

import androidx.compose.ui.text.font.FontFamily
import com.nguyenminhkhang.taskmanagement.R

enum class FontStyleOption(
    val key: String,
    val fontFamily: FontFamily,
    val labelRes: Int
) {
    DEFAULT("default", FontFamily.Default, R.string.font_default),
    SERIF("serif", FontFamily.Serif, R.string.font_serif),
    SANS_SERIF("sans_serif", FontFamily.SansSerif, R.string.font_sans_serif),
    MONOSPACE("monospace", FontFamily.Monospace, R.string.font_monospace),
    CURSIVE("cursive", FontFamily.Cursive, R.string.font_cursive);

    companion object {
        fun fromStorage(value: String?): FontStyleOption {
            if (value.isNullOrBlank()) return DEFAULT
            return entries.firstOrNull { it.key == value }
                ?: entries.firstOrNull { it.name == value }
                ?: DEFAULT
        }
    }
}
