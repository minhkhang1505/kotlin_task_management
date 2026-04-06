package com.nguyenminhkhang.taskmanagement.domain.usecase.auth

import com.nguyenminhkhang.taskmanagement.domain.repository.AuthRepository
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke() {
        authRepository.signOut()
        taskRepository.clearLocalData()
    }
}