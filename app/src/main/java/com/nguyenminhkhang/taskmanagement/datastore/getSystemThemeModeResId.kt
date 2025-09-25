package com.nguyenminhkhang.taskmanagement.datastore

import android.content.Context
import android.content.res.Configuration
import com.nguyenminhkhang.taskmanagement.R

fun getSystemThemeModeResId(context: Context): Int {
    return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> R.string.dark_mode
        Configuration.UI_MODE_NIGHT_NO -> R.string.light_mode
        else -> R.string.system_mode
    }
}