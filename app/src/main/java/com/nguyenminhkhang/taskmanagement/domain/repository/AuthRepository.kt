package com.nguyenminhkhang.taskmanagement.domain.repository

import com.nguyenminhkhang.taskmanagement.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val hasClaimedLocalTasksFlow: Flow<Boolean>
    suspend fun updateHasClaimedLocalTasks(hasClaimed: Boolean)
    suspend fun signInWithGoogleIdToken(idToken: String)
    suspend fun signInWithEmailAndPassword(email: String, password: String)
    fun getAuthState(): Flow<User?>
    suspend fun signOut()
}
