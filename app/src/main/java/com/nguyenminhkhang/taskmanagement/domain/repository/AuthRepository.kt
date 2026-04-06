package com.nguyenminhkhang.taskmanagement.domain.repository

import com.nguyenminhkhang.taskmanagement.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val hasClaimedLocalTasksFlow: Flow<Boolean>
    suspend fun updateHasClaimedLocalTasks(hasClaimed: Boolean)
    fun getAuthState(): Flow<User?>
    suspend fun signOut()
}