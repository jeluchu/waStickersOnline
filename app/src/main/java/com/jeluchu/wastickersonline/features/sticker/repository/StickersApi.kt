package com.jeluchu.wastickersonline.features.sticker.repository

import com.jeluchu.wastickersonline.features.sticker.models.PacksEntity
import retrofit2.http.GET

interface StickersApi {
    @GET("contents.json")
    suspend fun getStickers(): PacksEntity
}