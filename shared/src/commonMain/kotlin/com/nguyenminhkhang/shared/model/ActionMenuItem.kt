package com.nguyenminhkhang.shared.model

data class ActionMenuItem(
    val title: String,
    val action: () -> Unit
)