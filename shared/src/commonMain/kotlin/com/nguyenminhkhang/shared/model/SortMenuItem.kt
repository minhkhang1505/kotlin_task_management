package com.nguyenminhkhang.shared.model

data class SortMenuItem(
    val title: String,
    val sortedType: SortedType,
    val onClick: () -> Unit
)
