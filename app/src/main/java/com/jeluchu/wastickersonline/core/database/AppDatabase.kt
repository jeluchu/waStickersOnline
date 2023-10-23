package com.jeluchu.wastickersonline.core.database

import androidx.room.Database
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
}
