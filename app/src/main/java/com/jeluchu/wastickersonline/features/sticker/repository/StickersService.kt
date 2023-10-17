package com.jeluchu.wastickersonline.features.sticker.repository

import com.jeluchu.wastickersonline.features.sticker.models.PacksEntity
import retrofit2.Retrofit
import javax.inject.Inject

class StickersService @Inject constructor(
    retrofit: Retrofit
) : StickersApi {
    private val serviceApi by lazy { retrofit.create(StickersApi::class.java) }
    override suspend fun getStickers(): PacksEntity = serviceApi.getStickers()
}
