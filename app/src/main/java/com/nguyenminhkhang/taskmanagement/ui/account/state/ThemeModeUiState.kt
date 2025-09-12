package com.nguyenminhkhang.taskmanagement.ui.account.state

data class ThemeModeUiState(
    val radioOptions: List<String> = listOf("Light", "Dark", "System"),
    val selectedOption: String = "Light",
    val onOptionSelected: String = ""
)
