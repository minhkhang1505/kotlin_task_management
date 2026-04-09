package com.nguyenminhkhang.shared.usecase.auth

import com.nguyenminhkhang.shared.repository.AuthRepository
import com.nguyenminhkhang.shared.repository.TaskRepository
import kotlinx.coroutines.flow.first

class ProcessUserSignInUseCase (
    private val authRepository: AuthRepository,
    private val taskRepository: TaskRepository
) {
    suspend fun signInWithGoogle(idToken: String) {
        authRepository.signInWithGoogleIdToken(idToken)
        claimLocalTasksIfNeeded()
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String) {
        authRepository.signInWithEmailAndPassword(email, password)
        claimLocalTasksIfNeeded()
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
