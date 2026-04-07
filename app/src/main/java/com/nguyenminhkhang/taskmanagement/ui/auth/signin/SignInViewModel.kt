package com.nguyenminhkhang.taskmanagement.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.domain.usecase.auth.ProcessUserSignInUseCase
import com.nguyenminhkhang.taskmanagement.ui.auth.signin.state.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val processUserSignInUseCase: ProcessUserSignInUseCase
) : ViewModel() {

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _signInState.update { it.copy(isLoading = true) }

            try {
                processUserSignInUseCase.signInWithGoogle(idToken)
                _signInState.update { it.copy(isSuccess = true, isLoading = false) }

            } catch (e: Exception) {
                _signInState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _signInState.value = SignInState(
                error = "Email and password must not be empty"
            )
            return
        }

        viewModelScope.launch {
            _signInState.update { it.copy(isLoading = true) }

            try {
                processUserSignInUseCase.signInWithEmailAndPassword(email, password)
                _signInState.update { it.copy(isSuccess = true, isLoading = false) }
            } catch (e: Exception) {
                _signInState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun onEvent(event: SignInEvent) {
        when(event) {
            is SignInEvent.EmailChanged -> {
                _signInState.update {
                    it.copy(email = event.email)
                }
            }
            is SignInEvent.PasswordChanged -> {
                _signInState.update {
                    it.copy(password = event.password)
                }
            }
            is SignInEvent.SubmitSignInButton -> {
                signInWithEmailAndPassword(
                    _signInState.value.email,
                    _signInState.value.password
                )
            }
        }
    }
}

sealed class SignInEvent {
    data class EmailChanged(val email: String) : SignInEvent()
    data class PasswordChanged(val password: String) : SignInEvent()
    object SubmitSignInButton : SignInEvent()
}