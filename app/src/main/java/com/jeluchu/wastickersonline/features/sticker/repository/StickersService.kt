package com.jeluchu.wastickersonline.features.sticker.repository

import com.jeluchu.wastickersonline.features.sticker.models.PacksEntity
import retrofit2.Call
import retrofit2.Retrofit

class StickersService(retrofit: Retrofit): StickersApi {
    private val serviceApi by lazy { retrofit.create(StickersApi::class.java)  }
    override fun getStickers(): Call<PacksEntity> = serviceApi.getStickers()

}