package com.jeluchu.wastickersonline.core.di

import com.jeluchu.wastickersonline.features.stickerlist.usecase.GetStickers
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetStickers(get()) }
}