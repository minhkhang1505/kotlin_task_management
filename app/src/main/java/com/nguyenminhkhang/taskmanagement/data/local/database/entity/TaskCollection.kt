package com.nguyenminhkhang.taskmanagement.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nguyenminhkhang.taskmanagement.domain.model.SortedType

@Entity(tableName = "task_collection")
data class TaskCollection(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "user_id")
    val userId: String = "local_user",
    @ColumnInfo(name = "content")
    val content: String = "",
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "sorted_type", defaultValue = "0")
    val sortedType: Int = SortedType.SORTED_BY_DATE.value,
)