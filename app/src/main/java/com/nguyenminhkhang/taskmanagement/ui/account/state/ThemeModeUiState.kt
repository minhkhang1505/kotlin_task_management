package com.nguyenminhkhang.taskmanagement.ui.account.state

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.nguyenminhkhang.taskmanagement.R

data class ThemeModeUiState(
    @StringRes val radioOptions: List<Int> = listOf(
        R.string.light_mode,
        R.string.dark_mode,
        R.string.system_mode
    ),
    @StringRes val selectedOptionRes: Int = R.string.light_mode,
    val onOptionSelected: String = ""
)
