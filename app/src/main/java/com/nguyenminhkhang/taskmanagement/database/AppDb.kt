package com.nguyenminhkhang.taskmanagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nguyenminhkhang.taskmanagement.converter.Converters
import com.nguyenminhkhang.taskmanagement.database.dao.TaskDAO
import com.nguyenminhkhang.taskmanagement.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity

private const val DB_NAME = "task_db"
private const val DB_VERSION = 8

@Database(entities = [TaskEntity::class, TaskCollection::class], version = DB_VERSION)
@TypeConverters(Converters::class)
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
            .addMigrations(Migration_2_3)
            .addMigrations(MIGRATION_3_4)
            .addMigrations(MIGRATION_4_5)
            .addMigrations(MIGRATION_5_6)
            .addMigrations(MIGRATION_6_7)
            .addMigrations(MIGRATION_7_8)
            .build()
    }
}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        // Add new column sorted_type to task_collection table
        db.execSQL("ALTER TABLE task_collection ADD COLUMN sorted_type INTEGER NOT NULL DEFAULT 0")
    }
}

private val Migration_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE task ADD COLUMN start_date INTEGER DEFAULT NULL")
        db.execSQL("ALTER TABLE task ADD COLUMN due_date INTEGER DEFAULT NULL")
        db.execSQL("ALTER TABLE task ADD COLUMN reminder_time INTEGER NOT NULL DEFAULT 30")
        db.execSQL("ALTER TABLE task ADD COLUMN priority INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE task ADD COLUMN repeat_every TEXT DEFAULT NULL")
        db.execSQL("ALTER TABLE task ADD COLUMN repeat_days_of_week TEXT DEFAULT NULL")
        db.execSQL("ALTER TABLE task ADD COLUMN repeat_interval TEXT DEFAULT NULL")
        db.execSQL("ALTER TABLE task ADD COLUMN repeat_end_type TEXT DEFAULT NULL")
        db.execSQL("ALTER TABLE task ADD COLUMN repeat_end_date INTEGER DEFAULT NULL")
        db.execSQL("ALTER TABLE task ADD COLUMN repeat_end_count INTEGER DEFAULT NULL")
    }
}

private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE task ADD COLUMN start_time INTEGER DEFAULT NULL")
    }
}

private val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE task RENAME TO task_temp")

        db.execSQL(
            """
            CREATE TABLE task (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                is_favorite INTEGER NOT NULL,
                is_completed INTEGER NOT NULL,
                collection_id INTEGER NOT NULL,
                start_date INTEGER,
                due_date INTEGER,
                reminder_time INTEGER NOT NULL,
                priority INTEGER NOT NULL,
                repeat_every INTEGER NOT NULL,
                repeat_days_of_week TEXT,
                repeat_interval TEXT,
                repeat_end_type TEXT,
                repeat_end_date INTEGER,
                repeat_end_count INTEGER NOT NULL,
                start_time INTEGER,
                updated_at INTEGER NOT NULL,
                created_at INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            INSERT INTO task (
                id, title, is_favorite, is_completed, collection_id, start_date, due_date,
                reminder_time, priority, repeat_every, repeat_days_of_week, repeat_interval,
                repeat_end_type, repeat_end_date, repeat_end_count, start_time, updated_at, created_at
            )
            SELECT
                id,
                title,
                is_favorite,
                is_completed,
                collection_id,
                start_date,
                due_date,
                reminder_time,
                priority,
                CAST(COALESCE(repeat_every, '1') AS INTEGER), -- SỬA LẠI: Chuyển đổi an toàn từ TEXT sang INTEGER
                repeat_days_of_week,
                repeat_interval,
                repeat_end_type,
                repeat_end_date,
                COALESCE(repeat_end_count, 1), -- SỬA LẠI: Đảm bảo giá trị không null
                start_time,
                updated_at,
                created_at
            FROM task_temp
            """.trimIndent()
        )

        db.execSQL("DROP TABLE task_temp")
    }
}

private val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE task ADD COLUMN task_detail TEXT NOT NULL DEFAULT ''")
    }
}

private val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE task ADD COLUMN reminder_time_millis INTEGER DEFAULT NULL")
    }
}

private val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE task ADD COLUMN user_id TEXT NOT NULL DEFAULT 'local_user'")
        db.execSQL("ALTER TABLE task_collection ADD COLUMN user_id TEXT NOT NULL DEFAULT 'local_user'")
    }
}