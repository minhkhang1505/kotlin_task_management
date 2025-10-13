package com.nguyenminhkhang.taskmanagement.domain.model

data class ActionMenuItem(
    val title: String,
    val action: () -> Unit
)
