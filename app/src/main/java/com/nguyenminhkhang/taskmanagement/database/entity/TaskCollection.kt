package com.nguyenminhkhang.taskmanagement.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_collection")
data class TaskCollection(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "sorted_type", defaultValue = "0")
    val sortedType: Int,
)


enum class SortedType(val value: Int) {
    SORTED_BY_FAVORITE(1),
    SORTED_BY_DATE(0)
}

fun Int.toSortType() : SortedType {
    return when (this) {
        1-> SortedType.SORTED_BY_FAVORITE
        else -> SortedType.SORTED_BY_DATE
    }
}
