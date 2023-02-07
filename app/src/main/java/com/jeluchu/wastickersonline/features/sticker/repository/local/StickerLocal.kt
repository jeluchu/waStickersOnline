package com.jeluchu.wastickersonline.features.sticker.repository.local

import com.jeluchu.jchucomponents.core.platform.ContextHandler
import com.jeluchu.wastickersonline.core.database.AppDatabase
import com.jeluchu.wastickersonline.features.sticker.models.StickerPackEntity


class StickerLocal
(contextHandler: ContextHandler): StickerDBLocal {

    private val stickersApi by lazy { AppDatabase.getAppDatabase(contextHandler.appContext).stickerEntityDao() }

    override fun getStickers(): List<StickerPackEntity> = stickersApi.getStickers()
    override fun addStickers(stickerPackEntity: StickerPackEntity) = stickersApi.insertStickers(stickerPackEntity)
    override fun deleteAllStickers() = stickersApi.deleteAll()

}