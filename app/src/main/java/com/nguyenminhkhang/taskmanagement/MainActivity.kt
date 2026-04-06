package com.nguyenminhkhang.taskmanagement

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.nguyenminhkhang.taskmanagement.ui.navigation.TaskAppNavHost
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.SettingViewModel
import com.nguyenminhkhang.taskmanagement.ui.theme.TaskManagementTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(TAG).d("onCreate() - activity created")
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val settingViewModel = hiltViewModel<SettingViewModel>()
            val settingUiState = settingViewModel.settingsUiState.collectAsState()

            LaunchedEffect(settingUiState.value.languageRadioOption) {
                Timber.tag(TAG).d(
                    "Language effect triggered - selectedLanguage=%s",
                    settingUiState.value.languageRadioOption
                )
                val localList = LocaleListCompat.forLanguageTags(
                    settingUiState.value.languageRadioOption
                )
                AppCompatDelegate.setApplicationLocales(localList)
                Timber.tag(TAG).d(
                    "App locales applied - currentAppLocales=%s",
                    AppCompatDelegate.getApplicationLocales().toLanguageTags()
                )
            }

            TaskManagementTheme(settingViewModel) {
                TaskAppNavHost(
                    navController = navController,
                    settingViewModel = settingViewModel
                )
            }
        }
        requestNotificationPermissionIfNeeded()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}