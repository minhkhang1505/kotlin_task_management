package com.nguyenminhkhang.taskmanagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nguyenminhkhang.taskmanagement.database.dao.TaskDAO
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity

private const val DB_NAME = "task_db"
private const val DB_VERSION = 1

@Database(entities = [TaskEntity::class, TaskCollection::class], version = DB_VERSION)
abstract class AppDb : RoomDatabase()  {
    abstract fun taskDao(): TaskDAO

    companion object {
        private var instance: AppDb? = null

        operator fun invoke(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        fun buildDatabase(context: Context) : AppDb  = Room.databaseBuilder(
            context,
            AppDb::class.java,
            DB_NAME
        ).build()
    }
}