package com.nguyenminhkhang.taskmanagement.converter

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromSetToString(set: Set<String>?): String? {
        return set?.joinToString(separator = ",")
    }

    @TypeConverter
    fun fromStringToSet(value: String?): Set<String>? {
        return value?.split(",")?.toSet()
    }
}