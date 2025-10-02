package com.nguyenminhkhang.taskmanagement.ui.auth.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import com.nguyenminhkhang.taskmanagement.domain.repository.AuthRepository
import com.nguyenminhkhang.taskmanagement.ui.auth.signin.state.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class SignInViewModel @Inject constructor(private val taskRepository: TaskRepository, private val authRepository: AuthRepository ) : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _signInState.update { it.copy(isLoading = true) }

            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)

                val result = auth.signInWithCredential(credential).await()
                val user = result.user
                val hasAlreadyClaimed = authRepository.hasClaimedLocalTasksFlow.first()
                if(!hasAlreadyClaimed) {
                    taskRepository.claimLocalTasks()
                    taskRepository.claimLocalTaskCollection()
                    authRepository.updateHasClaimedLocalTasks(true)
                }

                Log.d("SignInViewModel", "signInWithGoogle: ${user?.email}")
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

        viewModelScope.launch(Dispatchers.IO) {
            _signInState.update { it.copy(isLoading = true) }

            try {
                auth.signInWithEmailAndPassword(email, password).await()
                val hasAlreadyClaimed = authRepository.hasClaimedLocalTasksFlow.first()
                if(!hasAlreadyClaimed) {
                    taskRepository.claimLocalTasks()
                    taskRepository.claimLocalTaskCollection()
                    authRepository.updateHasClaimedLocalTasks(true)
                }
                _signInState.update { it.copy(isSuccess = true, isLoading = false) }
                Log.d("SignInViewModel", "User signed in successfully by ${auth.currentUser?.email} and ${auth.currentUser?.uid}")
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