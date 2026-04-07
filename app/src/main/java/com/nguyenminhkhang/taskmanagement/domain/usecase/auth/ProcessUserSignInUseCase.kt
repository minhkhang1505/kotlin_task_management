package com.nguyenminhkhang.taskmanagement.domain.usecase.auth

import com.nguyenminhkhang.taskmanagement.domain.repository.AuthRepository
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProcessUserSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val taskRepository: TaskRepository
) {
    suspend fun signInWithGoogle(idToken: String) {
        withContext(Dispatchers.IO) {
            authRepository.signInWithGoogleIdToken(idToken)
            claimLocalTasksIfNeeded()
        }
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String) {
        withContext(Dispatchers.IO) {
            authRepository.signInWithEmailAndPassword(email, password)
            claimLocalTasksIfNeeded()
        }
    }

    private suspend fun claimLocalTasksIfNeeded() {
        val hasAlreadyClaimed = authRepository.hasClaimedLocalTasksFlow.first()
        if (!hasAlreadyClaimed) {
            taskRepository.claimLocalTasks()
            taskRepository.claimLocalTaskCollection()
            authRepository.updateHasClaimedLocalTasks(true)
        }
    }
}
