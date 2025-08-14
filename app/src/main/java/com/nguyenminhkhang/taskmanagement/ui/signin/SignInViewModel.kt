package com.nguyenminhkhang.taskmanagement.ui.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.nguyenminhkhang.taskmanagement.ui.signin.state.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _signInState.update { it.copy(isLoading = true) }

            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = suspendCancellableCoroutine<AuthResult> { cont ->
                    auth.signInWithCredential(credential)
                        .addOnSuccessListener { cont.resume(it, null) }
                        .addOnFailureListener { cont.resumeWithException(it) }
                }

                Log.d("SignInViewModel", "signInWithGoogle: ${result.user?.email}")
                _signInState.update { it.copy(isSuccess = true, isLoading = false) }

            } catch (e: Exception) {
                _signInState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}