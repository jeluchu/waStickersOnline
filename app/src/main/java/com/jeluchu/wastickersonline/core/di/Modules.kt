package com.jeluchu.wastickersonline.core.di

import com.jeluchu.wastickersonline.core.extensions.others.ContextHandler
import com.jeluchu.wastickersonline.core.platform.NetworkHandler
import com.jeluchu.wastickersonline.features.sticker.view.adapter.StickersAdapter
import com.jeluchu.wastickersonline.features.sticker.view.adapter.StickersDetailsAdapter
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { ContextHandler(get()) }
    factory { NetworkHandler(get()) }
    single { Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()) }
    factory { Dispatchers.IO }
}

val applicationModule = module(override = true) {
    factory { StickersAdapter() }
    factory { StickersDetailsAdapter() }
}