package com.nguyenminhkhang.taskmanagement.ui.settings.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.shared.model.SettingsPreferences
import com.nguyenminhkhang.shared.model.User
import com.nguyenminhkhang.shared.usecase.auth.ObserveAuthStateUseCase
import com.nguyenminhkhang.shared.usecase.auth.SignOutUseCase
import com.nguyenminhkhang.shared.usecase.settings.ObserveSettingsUseCase
import com.nguyenminhkhang.shared.usecase.settings.TrackSettingScreenViewUseCase
import com.nguyenminhkhang.shared.usecase.settings.UpdateColorThemeUseCase
import com.nguyenminhkhang.shared.usecase.settings.UpdateFontStyleUseCase
import com.nguyenminhkhang.shared.usecase.settings.UpdateLanguageUseCase
import com.nguyenminhkhang.shared.usecase.settings.UpdateThemeModeUseCase
import com.nguyenminhkhang.taskmanagement.ui.settings.FontStyleOption
import com.nguyenminhkhang.taskmanagement.ui.settings.LanguageOption
import com.nguyenminhkhang.taskmanagement.ui.settings.appearance.ColorThemeOption
import com.nguyenminhkhang.taskmanagement.ui.settings.appearance.ThemeModeOption
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.state.SettingUiState
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.state.ThemeModeUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingViewModel (
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updateThemeModeUseCase: UpdateThemeModeUseCase,
    private val updateFontStyleUseCase: UpdateFontStyleUseCase,
    private val updateColorThemeUseCase: UpdateColorThemeUseCase,
    private val trackSettingScreenViewUseCase: TrackSettingScreenViewUseCase
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
            observeAuthStateUseCase().collect { user ->
                updateUserState(user)
            }
        }

        viewModelScope.launch {
            observeSettingsUseCase().collect { prefs: SettingsPreferences ->
                val themeMode = ThemeModeOption.fromStorage(prefs.themeModeKey)
                val language = prefs.languageCode.ifBlank { LanguageOption.ENGLISH.code }
                val fontStyle = prefs.fontStyleKey.ifBlank { FontStyleOption.DEFAULT.key }
                val colorTheme = prefs.colorThemeKey.ifBlank { ColorThemeOption.PURPLE.key }
                Timber.tag(TAG).d(
                    "settingsFlow emit - languageCode=%s, themeModeKey=%s, fontStyleKey=%s, colorThemeKey=%s",
                    prefs.languageCode,
                    prefs.themeModeKey,
                    prefs.fontStyleKey,
                    prefs.colorThemeKey
                )
                _themeModeUiState.update { it.copy(selectedOption = themeMode) }
                _settingsUiState.update {
                    it.copy(
                        languageRadioOption = language,
                        fontStyleOption = fontStyle,
                        colorThemeOption = colorTheme
                    )
                }
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
        trackSettingScreenViewUseCase()
    }

    private fun changeLanguage(selectedLanguage: LanguageOption) {
        Timber.tag(TAG).d(
            "changeLanguage() - Current value: ${settingsUiState.value.languageRadioOption}, New value: $selectedLanguage"
        )
        viewModelScope.launch {
            updateLanguageUseCase(selectedLanguage.code)
            Timber.tag(TAG).d("changeLanguage() - saved language=%s", selectedLanguage.code)
        }
    }

    private suspend fun saveThemeMode(themeMode: ThemeModeOption) {
        updateThemeModeUseCase(themeMode.key)
    }

    fun onEvent(event: AccountEvent) {
        when (event) {
            is AccountEvent.SignOut -> {
                viewModelScope.launch {
                    signOutUseCase()
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
                    it.copy(selectedOption = event.mode)
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
                    updateFontStyleUseCase(event.fontStyle.key)
                }
            }
            is AccountEvent.ColorThemeChanged -> {
                Timber.tag(TAG).d("onEvent(ColorThemeChanged) - newColorTheme=%s", event.colorTheme.key)
                _settingsUiState.update {
                    it.copy(colorThemeOption = event.colorTheme.key)
                }
                viewModelScope.launch {
                    updateColorThemeUseCase(event.colorTheme.key)
                }
            }
        }
    }
}