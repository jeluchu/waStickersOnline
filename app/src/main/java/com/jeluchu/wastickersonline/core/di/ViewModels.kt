package com.jeluchu.wastickersonline.core.di

import com.jeluchu.wastickersonline.features.sticker.viewmodel.StickersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { StickersViewModel(get()) }
}
