package com.jeluchu.wastickersonline.core.di

import com.jeluchu.wastickersonline.features.sticker.usecase.GetStickers
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetStickers(get()) }
}