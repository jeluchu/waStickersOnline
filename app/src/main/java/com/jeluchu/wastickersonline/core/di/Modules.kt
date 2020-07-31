package com.jeluchu.wastickersonline.core.di

import com.jeluchu.wastickersonline.features.sticker.adapter.StickersDetailsAdapter
import com.jeluchu.wastickersonline.core.extensions.others.ContextHandler
import com.jeluchu.wastickersonline.core.extensions.retrofit.RetrofitClient
import com.jeluchu.wastickersonline.core.platform.NetworkHandler
import com.jeluchu.wastickersonline.features.sticker.adapter.StickersAdapter
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { ContextHandler(get()) }
    factory { NetworkHandler(get()) }
    single { RetrofitClient.buildRetrofit("https://aruppi.jeluchu.xyz/res/stickers/", GsonConverterFactory.create()) }
    single { Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()) }
    factory { Dispatchers.IO }
}

val applicationModule = module(override = true) {
    factory { StickersAdapter() }
    factory { StickersDetailsAdapter() }
}