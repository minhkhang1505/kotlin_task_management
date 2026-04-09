package com.nguyenminhkhang.taskmanagement.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nguyenminhkhang.shared.repository.TaskRepository

class UpdateTaskWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    lateinit var taskRepository: TaskRepository

    override suspend fun doWork(): Result {
        val taskId = inputData.getLong("TASK_ID", -1L)
        val isCompleted = inputData.getBoolean("IS_COMPLETED", false)

        if(taskId == -1L) {
            return Result.failure()
        }

        return try {
            taskRepository.updateTaskCompleted(taskId, isCompleted)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}