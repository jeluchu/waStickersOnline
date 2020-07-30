package com.jeluchu.wastickersonline.core.di

import com.jeluchu.wastickersonline.features.stickerlist.repository.StickersService
import org.koin.dsl.module

val dataSourceModule = module {
    factory { StickersService(get()) }
}