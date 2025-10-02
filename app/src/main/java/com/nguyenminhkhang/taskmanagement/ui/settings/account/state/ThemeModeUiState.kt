package com.nguyenminhkhang.taskmanagement.ui.settings.account.state

import androidx.annotation.StringRes
import com.nguyenminhkhang.taskmanagement.R

data class ThemeModeUiState(
    @StringRes val radioOptions: List<Int> = listOf(
        R.string.light_mode,
        R.string.dark_mode,
        R.string.system_mode
    ),
    @StringRes val selectedOptionRes: Int = R.string.light_mode,
    @StringRes val onOptionSelected: Int = R.string.light_mode
)
