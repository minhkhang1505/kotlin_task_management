package com.nguyenminhkhang.taskmanagement.converter

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromSetToString(set: Set<String>?): String? {
        return if (set.isNullOrEmpty()) null else set.joinToString(",")
    }


    @TypeConverter
    fun toSetFromString(value: String?): Set<String>? {
        return value?.takeIf { it.isNotBlank() }?.split(",")?.toSet()
    }
}