package com.nguyenminhkhang.taskmanagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nguyenminhkhang.taskmanagement.database.dao.TaskDAO
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity

private const val DB_NAME = "task_db"
private const val DB_VERSION = 2

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
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        // Add new column sorted_type to task_collection table
        db.execSQL("ALTER TABLE task_collection ADD COLUMN sorted_type INTEGER NOT NULL DEFAULT 0")
    }
}