package com.nguyenminhkhang.taskmanagement.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nguyenminhkhang.taskmanagement.ui.register.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun registerUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _registerState.value = RegisterState(
                errorMessage = "Email and password must not be empty"
            )
            return
        }

        viewModelScope.launch {
            _registerState.update { it.copy(isLoading = true) }

            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _registerState.update {
                    it.copy(
                        isSuccess = true,
                        isLoading = false
                    )
                }

                _navigationEvent.emit(NavigationEvent.NavigateToLogin)
                Log.d("RegisterViewModel", "User registered successfully by ${auth.currentUser?.email} and ${auth.currentUser?.uid}")
            } catch (e: Exception) {
                _registerState.update {
                    it.copy(
                        errorMessage = e.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onEvent(event: RegisterEvent) {
        when(event) {
            is RegisterEvent.EmailChanged -> {
                _registerState.update {
                    it.copy(email = event.email)
                }
            }
            is RegisterEvent.PasswordChanged -> {
                _registerState.update {
                    it.copy(password = event.password)
                }
            }
            is RegisterEvent.ConfirmPasswordChanged -> {
                _registerState.update {
                    it.copy(confirmPassword = event.confirmPassword)
                }
            }
            is RegisterEvent.TogglePasswordVisibility -> {
                _registerState.update {
                    it.copy(isPasswordVisible = !it.isPasswordVisible)
                }
            }
            is RegisterEvent.ToggleConfirmPasswordVisibility -> {
                _registerState.update {
                    it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
                }
            }
            is RegisterEvent.Submit -> {
                val currentState = _registerState.value
                if (currentState.password != currentState.confirmPassword) {
                    _registerState.update {
                        it.copy(errorMessage = "Passwords do not match")
                    }
                    return
                }
                registerUser(currentState.email, currentState.password)
            }
        }
    }
}

sealed class NavigationEvent {
    object NavigateToLogin : NavigationEvent()
}

sealed class RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegisterEvent()
    object TogglePasswordVisibility : RegisterEvent()
    object ToggleConfirmPasswordVisibility : RegisterEvent()
    object Submit : RegisterEvent()
}