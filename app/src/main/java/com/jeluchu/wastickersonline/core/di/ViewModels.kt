package com.jeluchu.wastickersonline.core.di

import com.jeluchu.wastickersonline.features.stickerlist.viewmodel.StickersViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { StickersViewModel(get()) }
}