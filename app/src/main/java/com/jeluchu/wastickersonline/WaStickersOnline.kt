package com.jeluchu.wastickersonline

import android.app.Application
import com.jeluchu.wastickersonline.core.extensions.hawk.initHawk
import com.jeluchu.wastickersonline.core.extensions.koin.initKoin
import com.jeluchu.wastickersonline.core.extensions.sharedprefs.initSharedPrefs

class WaStickersOnline : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
        initSharedPrefs()
        initHawk()

    }
}