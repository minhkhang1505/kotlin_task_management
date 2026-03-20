package com.nguyenminhkhang.taskmanagement.ui.home

data class AppMenuItem(
    val title: String,
    val action: () -> Unit
)