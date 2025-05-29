package com.nguyenminhkhang.taskmanagement.ui

data class AppMenuItem(
    val title: String,
    val action: () -> Unit
)
