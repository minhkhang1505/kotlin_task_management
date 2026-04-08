package com.nguyenminhkhang.taskmanagement.domain.usecase.auth

import com.nguyeminhkhang.shared.model.User
import com.nguyenminhkhang.taskmanagement.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase (
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> = authRepository.getAuthState()
}