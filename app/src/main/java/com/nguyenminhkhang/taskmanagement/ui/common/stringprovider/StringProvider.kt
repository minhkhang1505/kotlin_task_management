package com.nguyenminhkhang.taskmanagement.ui.common.stringprovider

import androidx.annotation.StringRes

interface StringProvider {
    fun getString(@StringRes id: Int): String
}