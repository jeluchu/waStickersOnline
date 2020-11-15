package com.jeluchu.wastickersonline.features.sticker.repository.local

import com.jeluchu.wastickersonline.features.sticker.models.StickerPackEntity

interface StickerDBLocal {
    fun getStickers(): List<StickerPackEntity>
    fun addStickers(stickerPackEntity: StickerPackEntity): Any
    fun deleteAllStickers(): Any
}