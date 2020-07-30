package com.jeluchu.wastickersonline.core.extensions.koin

import android.content.Context
import com.jeluchu.wastickersonline.core.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

fun Context.initKoin() {
    startKoin {
        androidLogger()
        androidContext(this@initKoin)
        modules(listOf(
                networkModule,
                applicationModule,
                dataSourceModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
        ))
    }
}