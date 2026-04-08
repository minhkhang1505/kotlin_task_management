package com.nguyenminhkhang.taskmanagement.data.mapper

import com.nguyeminhkhang.shared.model.toSortType
import com.nguyeminhkhang.shared.model.Collection
import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskCollection

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