package com.nguyenminhkhang.taskmanagement.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.nguyenminhkhang.taskmanagement.data.local.database.dao.TaskDAO
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import com.nguyenminhkhang.taskmanagement.data.repository.TaskRepositoryImpl
import com.nguyenminhkhang.taskmanagement.domain.repository.AuthRepository
import com.nguyenminhkhang.taskmanagement.data.repository.AuthRepositoryImpl
import com.nguyenminhkhang.taskmanagement.data.repository.SettingsRepositoryImpl
import com.nguyenminhkhang.taskmanagement.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("settings_preferences")
        }
    }
    @Singleton //tạo ra 1 thực thể duy nhất cho toàn bộ ứng dụng
    @Provides
    fun provideTaskRepo(taskDAO: TaskDAO): TaskRepository {
        return TaskRepositoryImpl(taskDAO)
    }

    @Singleton
    @Provides
    fun provideAuthRepo(@ApplicationContext context: Context): AuthRepository = AuthRepositoryImpl(context)

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore {
        val firestore = Firebase.firestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        firestore.firestoreSettings = settings
        return firestore
    }

    @Singleton
    @Provides
    fun provideSettingsRepo(
        dataStore: DataStore<Preferences>
    ): SettingsRepository {
        return SettingsRepositoryImpl(dataStore)
    }
}