package com.nguyenminhkhang.taskmanagement.ui.settings.settings

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsEvent
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsTracker
import com.nguyenminhkhang.taskmanagement.data.datastore.getSystemLanguageResId
import com.nguyenminhkhang.taskmanagement.data.datastore.getSystemThemeModeResId
import com.nguyenminhkhang.taskmanagement.domain.model.User
import com.nguyenminhkhang.taskmanagement.domain.repository.AuthRepository
import com.nguyenminhkhang.taskmanagement.domain.repository.SettingsRepository
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import com.nguyenminhkhang.taskmanagement.ui.settings.LanguageOption
import com.nguyenminhkhang.taskmanagement.ui.settings.FontStyleOption
import com.nguyenminhkhang.taskmanagement.ui.settings.appearance.ColorThemeOption
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.state.SettingUiState
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.state.ThemeModeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val taskRepository: TaskRepository,
    private val settingsRepository: SettingsRepository,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {
    companion object {
        private const val TAG = "SettingsViewModel"
    }

    private val _settingsUiState = MutableStateFlow(SettingUiState())
    val settingsUiState = _settingsUiState.asStateFlow()

    private val _themeModeUiState = MutableStateFlow(ThemeModeUiState())
    val themeUiState = _themeModeUiState.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    private fun updateUserState(user: User?) {
        _settingsUiState.update { currentState ->
            currentState.copy(
                isLoggedIn = user != null,
                userName = user?.displayName ?: "UnidentifiedUser",
                userEmail = user?.email,
                userAvatarUrl = user?.photoUrl,
            )
        }
    }

    init {
        Timber.tag(TAG).d("SettingViewModel created")

        viewModelScope.launch {
            authRepository.getAuthState().collect { user ->
                updateUserState(user)
            }
        }

        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { prefs ->
                val themeMode = prefs.themeModeRes ?: getSystemThemeModeResId(context)
                val language = prefs.languageCode ?: getSystemLanguageResId()
                val fontStyle = prefs.fontStyleKey ?: FontStyleOption.DEFAULT.key
                val colorTheme = prefs.colorThemeKey ?: ColorThemeOption.PURPLE.key
                Timber.tag(TAG).d(
                    "settingsFlow emit - languageCode=%s, resolvedLanguage=%s, themeMode=%s, fontStyle=%s, colorTheme=%s",
                    prefs.languageCode,
                    language,
                    themeMode,
                    fontStyle,
                    colorTheme
                )
                _themeModeUiState.update { it.copy(selectedOptionRes = themeMode) }
                _settingsUiState.update { it.copy(
                    languageRadioOption = language,
                    fontStyleOption = fontStyle,
                    colorThemeOption = colorTheme
                ) }
                Timber.tag(TAG).d(
                    "uiState updated - languageRadioOption=%s, fontStyleOption=%s, colorThemeOption=%s",
                    _settingsUiState.value.languageRadioOption,
                    _settingsUiState.value.fontStyleOption,
                    _settingsUiState.value.colorThemeOption
                )
            }
        }
    }

    fun onScreenShow() {
        analyticsTracker.trackEvent(
            AnalyticsEvent.ScreenView("SettingScreen")
        )
    }

    private fun changeLanguage(selectedLanguage: LanguageOption) {
        Timber.tag(TAG).d(
            "changeLanguage() - Current value: ${settingsUiState.value.languageRadioOption}, New value: $selectedLanguage"
        )
        viewModelScope.launch {
            settingsRepository.setLanguage(selectedLanguage)
            Timber.tag(TAG).d("changeLanguage() - saved language=%s", selectedLanguage.code)
        }
    }

    private suspend fun saveThemeMode(@StringRes themeMode: Int) {
        settingsRepository.setThemeMode(themeMode)
    }

    fun onEvent(event: AccountEvent) {
        when (event) {
            is AccountEvent.SignOut -> {
                viewModelScope.launch(Dispatchers.IO) {
                    authRepository.signOut()
                    taskRepository.clearLocalData()
                    _logoutEvent.emit(Unit)
                }
            }
            is AccountEvent.HideLogoutDialog -> {
                _settingsUiState.update { currentState ->
                    currentState.copy(isLogoutDialogVisible = false)
                }
            }
            is AccountEvent.ShowLogoutDialog -> {
                _settingsUiState.update { currentState ->
                    currentState.copy(isLogoutDialogVisible = true)
                }
            }
            is AccountEvent.DismissLogoutDialog -> {
                _settingsUiState.update { currentState ->
                    currentState.copy(isLogoutDialogVisible = false)
                }
            }
            is AccountEvent.ThemeModeChanged -> {
                _themeModeUiState.update {
                    it.copy(selectedOptionRes = event.mode)
                }
            }
            is AccountEvent.SaveThemeMode -> {
                viewModelScope.launch {
                    saveThemeMode(event.mode)
                }
            }
            is AccountEvent.LanguageChanged -> {
                Timber.tag(TAG).d("onEvent(LanguageChanged) - newLanguage=%s", event.language.code)
                _settingsUiState.update {
                    it.copy(languageRadioOption = event.language.code)
                }
                changeLanguage(event.language)
            }
            is AccountEvent.FontStyleChanged -> {
                Timber.tag(TAG).d("onEvent(FontStyleChanged) - newFontStyle=%s", event.fontStyle.key)
                _settingsUiState.update { 
                    it.copy(fontStyleOption = event.fontStyle.key)
                }
                viewModelScope.launch {
                    settingsRepository.setFontStyle(event.fontStyle.key)
                }
            }
            is AccountEvent.ColorThemeChanged -> {
                Timber.tag(TAG).d("onEvent(ColorThemeChanged) - newColorTheme=%s", event.colorTheme.key)
                _settingsUiState.update { 
                    it.copy(colorThemeOption = event.colorTheme.key)
                }
                viewModelScope.launch {
                    settingsRepository.setColorTheme(event.colorTheme.key)
                }
            }
        }
    }
}