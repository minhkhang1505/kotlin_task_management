package com.nguyenminhkhang.taskmanagement.ui.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.repository.authrepository.AuthRepo
import com.nguyenminhkhang.taskmanagement.ui.signin.state.SignInState
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
class SignInViewModel @Inject constructor( private val taskRepo: TaskRepo, private val authRepo: AuthRepo ) : ViewModel() {
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
                val hasAlreadyClaimed = authRepo.hasClaimedLocalTasksFlow.first()
                if(!hasAlreadyClaimed) {
                    taskRepo.claimLocalTasks()
                    taskRepo.claimLocalTaskCollection()
                    authRepo.updateHasClaimedLocalTasks(true)
                }

                Log.d("SignInViewModel", "signInWithGoogle: ${user?.email}")
                _signInState.update { it.copy(isSuccess = true, isLoading = false) }

            } catch (e: Exception) {
                _signInState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}