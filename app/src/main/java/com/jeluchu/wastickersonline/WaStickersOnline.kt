package com.jeluchu.wastickersonline

import android.app.Application
import com.jeluchu.wastickersonline.core.extensions.koin.initKoin
import com.orhanobut.hawk.Hawk

class WaStickersOnline : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()

        Hawk.init(this).build()
    }
}