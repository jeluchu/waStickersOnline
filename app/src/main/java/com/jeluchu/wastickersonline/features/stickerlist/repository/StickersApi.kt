package com.jeluchu.wastickersonline.features.stickerlist.repository

import com.jeluchu.wastickersonline.features.stickerlist.models.PacksEntity
import retrofit2.Call
import retrofit2.http.GET

interface StickersApi {

    @GET("example.json")
    fun getStickers(): Call<PacksEntity>

}