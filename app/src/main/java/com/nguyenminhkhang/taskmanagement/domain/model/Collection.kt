package com.nguyenminhkhang.taskmanagement.domain.model

data class Collection(
    val id: Long? = null,
    val userId: String = "local_user",
    val content: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
    val sortedType: SortedType = SortedType.SORTED_BY_DATE
)
