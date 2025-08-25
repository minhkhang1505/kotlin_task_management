package com.nguyenminhkhang.taskmanagement.converter

import androidx.room.TypeConverter

class Converters {

//    @TypeConverter
//    fun fromSetToString(set: Set<String>?): String? {
//        return if (set.isNullOrEmpty()) null else set.joinToString(",")
//    }
//
//
//    @TypeConverter
//    fun toSetFromString(value: String?): Set<String>? {
//        return value?.takeIf { it.isNotBlank() }?.split(",")?.toSet()
//    }

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.joinToString(",") // ví dụ: "Mon,Tue,Wed"
    }

    @TypeConverter
    fun toList(data: String?): List<String>? {
        return data?.split(",")?.filter { it.isNotEmpty() }
    }
}