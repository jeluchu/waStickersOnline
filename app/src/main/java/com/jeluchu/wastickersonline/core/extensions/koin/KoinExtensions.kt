package com.jeluchu.wastickersonline.core.extensions.koin

import android.content.Context
import com.jeluchu.wastickersonline.core.di.applicationModule
import com.jeluchu.wastickersonline.core.di.dataSourceModule
import com.jeluchu.wastickersonline.core.di.databaseModule
import com.jeluchu.wastickersonline.core.di.networkModule
import com.jeluchu.wastickersonline.core.di.repositoryModule
import com.jeluchu.wastickersonline.core.di.useCaseModule
import com.jeluchu.wastickersonline.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun Context.initKoin() {
    startKoin {
        androidContext(this@initKoin)
        modules(
            networkModule,
            databaseModule,
            applicationModule,
            dataSourceModule,
            repositoryModule,
            useCaseModule,
            viewModelModule
        )
    }
}