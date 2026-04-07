package com.nguyenminhkhang.taskmanagement.domain.usecase.auth

import com.nguyenminhkhang.taskmanagement.domain.repository.AuthRepository
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignOutUseCase (
    private val authRepository: AuthRepository,
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            authRepository.signOut()
            taskRepository.clearLocalData()
        }
    }
}