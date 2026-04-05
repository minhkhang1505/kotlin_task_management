package com.nguyenminhkhang.taskmanagement.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.settings.account.SettingViewModel
import com.nguyenminhkhang.taskmanagement.ui.settings.FontStyleOption
import com.nguyenminhkhang.taskmanagement.ui.settings.appearance.ColorThemeOption

private val PurpleDarkColorScheme = darkColorScheme(primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80)
private val PurpleLightColorScheme = lightColorScheme(primary = Purple40, secondary = PurpleGrey40, tertiary = Pink40)

private val RedDarkColorScheme = darkColorScheme(primary = Red80, secondary = RedGrey80, tertiary = RedPink80)
private val RedLightColorScheme = lightColorScheme(primary = Red40, secondary = RedGrey40, tertiary = RedPink40)

private val GreenDarkColorScheme = darkColorScheme(primary = Green80, secondary = GreenGrey80, tertiary = GreenLime80)
private val GreenLightColorScheme = lightColorScheme(primary = Green40, secondary = GreenGrey40, tertiary = GreenLime40)

private val BlueDarkColorScheme = darkColorScheme(primary = Blue80, secondary = BlueGrey80, tertiary = BlueLight80)
private val BlueLightColorScheme = lightColorScheme(primary = Blue40, secondary = BlueGrey40, tertiary = BlueLight40)

private val OrangeDarkColorScheme = darkColorScheme(primary = Orange80, secondary = OrangeGrey80, tertiary = OrangeYellow80)
private val OrangeLightColorScheme = lightColorScheme(primary = Orange40, secondary = OrangeGrey40, tertiary = OrangeYellow40)

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