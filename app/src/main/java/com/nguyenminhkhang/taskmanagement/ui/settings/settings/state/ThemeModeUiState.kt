package com.nguyenminhkhang.taskmanagement.ui.settings.settings.state

import com.nguyenminhkhang.shared.model.settings.ThemeModeOption

data class ThemeModeUiState(
    val radioOptions: List<ThemeModeOption> = ThemeModeOption.entries,
    val selectedOption: ThemeModeOption = ThemeModeOption.LIGHT
)
