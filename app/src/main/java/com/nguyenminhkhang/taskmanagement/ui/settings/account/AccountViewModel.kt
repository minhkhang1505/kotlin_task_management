package com.nguyenminhkhang.taskmanagement.ui.settings.account

import android.content.Context
import androidx.annotation.StringRes
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.datastore.AccountDataStoreKeys.LANGUAGE_KEY
import com.nguyenminhkhang.taskmanagement.datastore.AccountDataStoreKeys.THEME_MODE_KEY
import com.nguyenminhkhang.taskmanagement.datastore.dataStore
import com.nguyenminhkhang.taskmanagement.datastore.getSystemLanguageResId
import com.nguyenminhkhang.taskmanagement.datastore.getSystemThemeModeResId
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import com.nguyenminhkhang.taskmanagement.ui.settings.account.state.AccountUiState
import com.nguyenminhkhang.taskmanagement.ui.settings.account.state.ThemeModeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState = _uiState.asStateFlow()

    private val _themeModeUiState = MutableStateFlow(ThemeModeUiState())
    val themeUiState = _themeModeUiState.asStateFlow()

    private val auth: FirebaseAuth = Firebase.auth

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    private val settingsDataStore = context.dataStore

    private fun getUserInfo (): String {
        return auth.currentUser?.email ?: "UnidentifiedUser"
    }

    private fun getLanguageCode(@StringRes languageRes: Int): String {
        return when (languageRes) {
            R.string.language_english -> "en"
            R.string.language_vietnamese -> "vi"
            else -> Locale.getDefault().language
        }
    }

    init {
        _uiState.value = AccountUiState(
            userEmail = getUserInfo(),
            userAvatarUrl = auth.currentUser?.photoUrl?.toString()
        )

        viewModelScope.launch {
            settingsDataStore.data.collect { prefs ->
                val themeMode = prefs[THEME_MODE_KEY] ?: getSystemThemeModeResId(context)
                val language = prefs[LANGUAGE_KEY] ?: getSystemLanguageResId()
                _themeModeUiState.update { it.copy(selectedOptionRes = themeMode) }
                _uiState.update { it.copy(selectedLanguage = language) }
            }

        }
    }

    private suspend fun saveThemeMode(@StringRes themeMode: Int) {
        settingsDataStore.edit { reference ->
            reference[THEME_MODE_KEY] = themeMode
        }
    }

    private suspend fun saveLanguage(@StringRes language: Int) {
        settingsDataStore.edit { reference ->
            reference[LANGUAGE_KEY] = language
        }
    }

    fun onEvent(event: AccountEvent) {
        when (event) {
            is AccountEvent.SignOut -> {
                viewModelScope.launch(Dispatchers.IO) {
                    auth.signOut()
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                    GoogleSignIn.getClient(context, gso).signOut().await()
                    taskRepository.clearLocalData()
                    _logoutEvent.emit(Unit)
                }
            }
            is AccountEvent.ShowLogoutDialog -> {
                _uiState.update { currentState ->
                    currentState.copy(isLogoutDialogVisible = true)
                }
            }
            is AccountEvent.HideLogoutDialog -> {
                _uiState.update { currentState ->
                    currentState.copy(isLogoutDialogVisible = false)
                }
            }
            is AccountEvent.DismissLogoutDialog -> {
                _uiState.update { currentState ->
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
                _uiState.update {
                    it.copy(selectedLanguage = event.language)
                }
                viewModelScope.launch {
                    saveLanguage(event.language)
                    _logoutEvent.emit(Unit)
                }
            }
        }
    }
}