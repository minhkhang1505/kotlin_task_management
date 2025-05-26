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
    val updatedAt: Long
)

