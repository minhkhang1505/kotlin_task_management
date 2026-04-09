package com.nguyenminhkhang.shared.usecase.auth

import com.nguyenminhkhang.shared.repository.AuthRepository
import com.nguyenminhkhang.shared.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignOutUseCase (
    private val authRepository: AuthRepository,
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke() {
        authRepository.signOut()
        taskRepository.clearLocalData()
    }
}