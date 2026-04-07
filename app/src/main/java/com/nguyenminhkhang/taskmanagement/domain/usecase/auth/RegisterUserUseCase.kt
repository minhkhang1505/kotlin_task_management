package com.nguyenminhkhang.taskmanagement.domain.usecase.auth

import com.nguyenminhkhang.taskmanagement.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class RegisterUserResult {
    object Success : RegisterUserResult()
    data class ValidationError(val message: String) : RegisterUserResult()
}

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String
    ): RegisterUserResult {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return RegisterUserResult.ValidationError("Email and password must not be empty")
        }

        if (password != confirmPassword) {
            return RegisterUserResult.ValidationError("Passwords do not match")
        }

        return withContext(Dispatchers.IO) {
            authRepository.createUserWithEmailAndPassword(email, password)
            RegisterUserResult.Success
        }
    }
}
