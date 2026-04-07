package com.nguyenminhkhang.taskmanagement.domain.usecase.auth

import com.nguyenminhkhang.taskmanagement.domain.model.User
import com.nguyenminhkhang.taskmanagement.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAuthStateUseCase (
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> = authRepository.getAuthState()
}