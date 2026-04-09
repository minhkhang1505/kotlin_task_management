package com.nguyenminhkhang.shared.usecase.auth

import com.nguyenminhkhang.shared.model.User
import com.nguyenminhkhang.shared.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase (
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> = authRepository.getAuthState()
}