package com.nguyenminhkhang.taskmanagement

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepo: TaskRepo,
    private val taskRepo1: TaskRepo
) : ViewModel() {
    init {
        Log.d("MainViewModel", "MainViewModel created with taskRepo: $taskRepo")
        Log.d("MainViewModel", "MainViewModel created with taskRepo1: $taskRepo1")
    }
}