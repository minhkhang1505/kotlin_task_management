package com.nguyenminhkhang.shared.usecase.auth

import com.nguyenminhkhang.shared.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class RegisterUserResult {
    object Success : RegisterUserResult()
    data class ValidationError(val message: String) : RegisterUserResult()
}

class RegisterUserUseCase (
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

        return try {
            authRepository.createUserWithEmailAndPassword(email, password)
            RegisterUserResult.Success
        } catch (e: Exception) {
            RegisterUserResult.ValidationError("An error occur when create user with email and password $e")
        }
    }
}
