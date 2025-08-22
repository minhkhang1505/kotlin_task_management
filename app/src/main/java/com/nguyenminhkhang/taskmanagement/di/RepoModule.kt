package com.nguyenminhkhang.taskmanagement.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.nguyenminhkhang.taskmanagement.database.dao.TaskDAO
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.repository.TaskRepoImpl
import com.nguyenminhkhang.taskmanagement.repository.authrepository.AuthRepo
import com.nguyenminhkhang.taskmanagement.repository.authrepository.AuthRepoImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Singleton //tạo ra 1 thực thể duy nhất cho toàn bộ ứng dụng
    @Provides
    fun provideTaskRepo(taskDAO: TaskDAO): TaskRepo {
        return TaskRepoImpl(taskDAO)
    }

    @Singleton
    @Provides
    fun provideAuthRepo(@ApplicationContext context: Context): AuthRepo = AuthRepoImpl(context)

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
}