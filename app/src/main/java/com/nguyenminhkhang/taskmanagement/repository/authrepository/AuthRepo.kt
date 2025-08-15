package com.nguyenminhkhang.taskmanagement.repository.authrepository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    val hasClaimedLocalTasksFlow: Flow<Boolean>
    suspend fun updateHasClaimedLocalTasks(hasClaimed: Boolean)
    fun getAuthState(): Flow<FirebaseUser?>
}