package com.nguyenminhkhang.taskmanagement.domain.model

data class User(
    val uid: String,
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val isEmailVerified: Boolean = false,
    val isAnonymous: Boolean = false
)
