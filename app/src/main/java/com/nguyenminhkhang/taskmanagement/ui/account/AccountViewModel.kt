package com.nguyenminhkhang.taskmanagement.ui.account

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.account.state.AccountUiState
import com.nguyenminhkhang.taskmanagement.ui.account.state.ThemeModeUiState
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
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val taskRepo: TaskRepo
) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState = _uiState.asStateFlow()

    private val _themeModeUiState = MutableStateFlow(ThemeModeUiState())
    val themeUiState = _themeModeUiState.asStateFlow()

    private val auth: FirebaseAuth = Firebase.auth

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    private fun getUserInfo (): String {
        return auth.currentUser?.email ?: "UnidentifiedUser"
    }

    init {
        _uiState.value = AccountUiState(
            userEmail = getUserInfo(),
            userAvatarUrl = auth.currentUser?.photoUrl?.toString()
        )
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
                    taskRepo.clearLocalData()
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
                _themeModeUiState.update { currentState ->
                    currentState.copy(selectedOption = event.mode )
                }
            }
        }
    }
}