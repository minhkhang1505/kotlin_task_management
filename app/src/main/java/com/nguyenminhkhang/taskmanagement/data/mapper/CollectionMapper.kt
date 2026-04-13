package com.nguyenminhkhang.taskmanagement.data.mapper

import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskCollection
import com.nguyenminhkhang.shared.model.toSortType
import com.nguyenminhkhang.shared.model.Collection

fun TaskCollection.toDomain(): Collection = Collection(
    id = id,
    userId = userId,
    content = content,
    updatedAt = updatedAt,
    sortedType = sortedType.toSortType()
)

fun Collection.toEntity(): TaskCollection = TaskCollection(
    id = id,
    userId = userId,
    content = content,
    updatedAt = updatedAt,
    sortedType = sortedType.value
)