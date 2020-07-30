package com.jeluchu.wastickersonline.app

import android.app.Application
import com.orhanobut.hawk.Hawk

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
    }
}