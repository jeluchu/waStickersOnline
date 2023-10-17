package com.jeluchu.wastickersonline.features.sticker.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeluchu.wastickersonline.features.sticker.models.StickerPackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StickersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStickers(newsEntity: StickerPackEntity)

    @Query("DELETE FROM StickerPackEntity")
    fun deleteAll()

    @Query("SELECT * FROM StickerPackEntity")
    fun getStickers(): Flow<List<StickerPackEntity>>

    @Query("SELECT * from StickerPackEntity")
    fun getStickersForServerQuery(): List<StickerPackEntity>?
}