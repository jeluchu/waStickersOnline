package com.jeluchu.wastickersonline.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jeluchu.wastickersonline.core.utils.room.ListStringConverter
import com.jeluchu.wastickersonline.features.sticker.models.StickerPackEntity
import com.jeluchu.wastickersonline.features.sticker.repository.local.StickersDao

@Database(
    entities = [
        StickerPackEntity::class,
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(value = [ListStringConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun stickerDao(): StickersDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "waStickersDB"
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            instance
        }
    }
}
