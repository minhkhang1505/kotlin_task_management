package com.nguyenminhkhang.taskmanagement.ui.common.stringprovider

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidStringProvider @Inject constructor(
    private val context: Context
): StringProvider {
    override fun getString(id: Int): String {
        return context.getString(id)
    }
}