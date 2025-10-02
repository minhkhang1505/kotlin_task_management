package com.nguyenminhkhang.taskmanagement.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val hasClaimedLocalTasksFlow: Flow<Boolean>
    suspend fun updateHasClaimedLocalTasks(hasClaimed: Boolean)
    fun getAuthState(): Flow<FirebaseUser?>
}