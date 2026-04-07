package com.nguyenminhkhang.taskmanagement.di.module

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.nguyenminhkhang.taskmanagement.data.repository.AuthRepositoryImpl
import com.nguyenminhkhang.taskmanagement.data.repository.SettingsRepositoryImpl
import com.nguyenminhkhang.taskmanagement.data.repository.TaskRepositoryImpl
import com.nguyenminhkhang.taskmanagement.domain.repository.AuthRepository
import com.nguyenminhkhang.taskmanagement.domain.repository.SettingsRepository
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile("settings_preferences")
        }
    }

    single<TaskRepository> { TaskRepositoryImpl(get()) }

    single<AuthRepository> { AuthRepositoryImpl(androidContext()) }

    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

    single<FirebaseFirestore> {
        Firebase.firestore.apply {
            firestoreSettings = firestoreSettings {
                isPersistenceEnabled = true
            }
        }
    }
}