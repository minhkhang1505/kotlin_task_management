package com.nguyenminhkhang.taskmanagement.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.compose.OrangeDarkColorScheme
import com.example.compose.OrangeLightColorScheme
import com.example.compose.PurpleDarkColorScheme
import com.example.compose.PurpleLightColorScheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.SettingViewModel
import com.nguyenminhkhang.taskmanagement.ui.settings.FontStyleOption
import com.nguyenminhkhang.taskmanagement.ui.settings.appearance.ColorThemeOption

@Composable
fun TaskManagementTheme(
    viewModel: SettingViewModel,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val uiState by viewModel.themeUiState.collectAsState()
    val settingUiState by viewModel.settingsUiState.collectAsState()
    val selectedThemeMode = uiState.selectedOptionRes
    val fontStyle = settingUiState.fontStyleOption
    val colorThemeKey = settingUiState.colorThemeOption

    val isDarkTheme = when (selectedThemeMode) {
        R.string.dark_mode -> true
        R.string.light_mode -> false
        R.string.system_mode -> isSystemInDarkTheme()
        else -> isSystemInDarkTheme() // Mặc định
    }

    val colorThemeOption = ColorThemeOption.fromStorage(colorThemeKey)

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && colorThemeOption == ColorThemeOption.PURPLE -> {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDarkTheme -> when (colorThemeOption) {
            ColorThemeOption.PURPLE -> PurpleDarkColorScheme
            ColorThemeOption.RED -> RedDarkColorScheme
            ColorThemeOption.GREEN -> GreenDarkColorScheme
            ColorThemeOption.BLUE -> BlueDarkColorScheme
            ColorThemeOption.ORANGE -> OrangeDarkColorScheme
        }
        else -> when (colorThemeOption) {
            ColorThemeOption.PURPLE -> PurpleLightColorScheme
            ColorThemeOption.RED -> RedLightColorScheme
            ColorThemeOption.GREEN -> GreenLightColorScheme
            ColorThemeOption.BLUE -> BlueLightColorScheme
            ColorThemeOption.ORANGE -> OrangeLightColorScheme
        }
    }

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isDarkTheme

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
    }

    val fontFamily = FontStyleOption.fromStorage(fontStyle).fontFamily

    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography(fontFamily),
        content = content
    )
}