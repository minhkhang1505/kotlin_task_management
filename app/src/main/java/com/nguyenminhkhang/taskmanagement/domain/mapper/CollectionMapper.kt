package com.nguyenminhkhang.taskmanagement.domain.mapper

import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.domain.model.Collection
import com.nguyenminhkhang.taskmanagement.domain.model.toSortType

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