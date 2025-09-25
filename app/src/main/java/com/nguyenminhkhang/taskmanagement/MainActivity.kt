package com.nguyenminhkhang.taskmanagement

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nguyenminhkhang.taskmanagement.helper.LocaleHelper
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.TaskApp
import com.nguyenminhkhang.taskmanagement.ui.account.AccountViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskApp( )
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

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val langCode = prefs.getString("language_code", Locale.getDefault().language) ?: "en"

        val context = LocaleHelper.updateLocale(newBase, langCode)
        super.attachBaseContext(context)
    }

    private fun getLanguageCodeFromRes(@StringRes languageRes: Int): String {
        return when (languageRes) {
            R.string.language_english -> "en"
            R.string.language_vietnamese -> "vi"
            else -> Locale.getDefault().language
        }
    }
}