package com.jeluchu.wastickersonline.core.di

import com.jeluchu.jchucomponents.core.platform.ContextHandler
import com.jeluchu.jchucomponents.ktx.context.handler.NetworkHandler
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

val applicationModule = module {
    factory { StickersAdapter() }
    factory { StickersDetailsAdapter() }
}