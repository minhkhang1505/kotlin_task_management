package com.nguyenminhkhang.taskmanagement.ui.settings.settings.state

import com.nguyenminhkhang.taskmanagement.ui.settings.appearance.ThemeModeOption

data class ThemeModeUiState(
    val radioOptions: List<ThemeModeOption> = ThemeModeOption.entries,
    val selectedOption: ThemeModeOption = ThemeModeOption.LIGHT
)
