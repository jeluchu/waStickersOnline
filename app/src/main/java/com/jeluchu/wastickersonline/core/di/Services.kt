package com.jeluchu.wastickersonline.core.di

import com.jeluchu.wastickersonline.features.sticker.repository.StickersService
import org.koin.dsl.module

val dataSourceModule = module {
    factory { StickersService(get()) }
}