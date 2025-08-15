package com.nguyenminhkhang.taskmanagement.repository.authrepository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class AuthRepoImpl @Inject constructor(
    @ApplicationContext private val context: Context
) :AuthRepo {
    private val auth: FirebaseAuth = Firebase.auth

    private object PreferencesKeys {
        val HAS_CLAIMED_LOCAL_TASKS = booleanPreferencesKey("has_claimed_local_tasks")
    }

    override val hasClaimedLocalTasksFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.HAS_CLAIMED_LOCAL_TASKS] ?: false
        }

    override suspend fun updateHasClaimedLocalTasks(hasClaimed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_CLAIMED_LOCAL_TASKS] = hasClaimed
        }
    }

    override fun getAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser).isSuccess
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }
}