package com.jeluchu.wastickersonline.core.di

import com.jeluchu.wastickersonline.features.sticker.repository.local.StickerLocal
import org.koin.dsl.module

val databaseModule = module { factory { StickerLocal(get()) } }