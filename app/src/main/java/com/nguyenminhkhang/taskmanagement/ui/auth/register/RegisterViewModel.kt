package com.nguyenminhkhang.taskmanagement.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.shared.usecase.auth.RegisterUserResult
import com.nguyenminhkhang.shared.usecase.auth.RegisterUserUseCase
import com.nguyenminhkhang.taskmanagement.ui.auth.register.state.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerState = _registerUiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()

    fun registerUser(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _registerUiState.update { it.copy(isLoading = true) }

            try {
                when (val result = registerUserUseCase(email, password, confirmPassword)) {
                    is RegisterUserResult.Success -> {
                        _registerUiState.update {
                            it.copy(
                                isSuccess = true,
                                isLoading = false,
                                errorMessage = null
                            )
                        }

                        _navigationEvent.emit(NavigationEvent.NavigateToLogin)
                    }

                    is RegisterUserResult.ValidationError -> {
                        _registerUiState.update {
                            it.copy(
                                errorMessage = result.message,
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _registerUiState.update {
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
                _registerUiState.update {
                    it.copy(email = event.email)
                }
            }
            is RegisterEvent.PasswordChanged -> {
                _registerUiState.update {
                    it.copy(password = event.password)
                }
            }
            is RegisterEvent.ConfirmPasswordChanged -> {
                _registerUiState.update {
                    it.copy(confirmPassword = event.confirmPassword)
                }
            }
            is RegisterEvent.TogglePasswordVisibility -> {
                _registerUiState.update {
                    it.copy(isPasswordVisible = !it.isPasswordVisible)
                }
            }
            is RegisterEvent.ToggleConfirmPasswordVisibility -> {
                _registerUiState.update {
                    it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
                }
            }
            is RegisterEvent.Submit -> {
                val currentState = _registerUiState.value
                registerUser(
                    currentState.email,
                    currentState.password,
                    currentState.confirmPassword
                )
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