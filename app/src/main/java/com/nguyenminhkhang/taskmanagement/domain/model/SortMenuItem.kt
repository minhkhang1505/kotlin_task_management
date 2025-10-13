package com.nguyenminhkhang.taskmanagement.domain.model

data class SortMenuItem(
    val title: String,
    val sortedType: SortedType,
    val onClick: () -> Unit
)
