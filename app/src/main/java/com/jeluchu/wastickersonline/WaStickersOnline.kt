package com.jeluchu.wastickersonline

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.jeluchu.wastickersonline.core.extensions.sharedprefs.initSharedPrefs
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WaStickersOnline : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        initSharedPrefs()
    }

    init {
        instance = this
    }

    companion object {
        var instance: WaStickersOnline? = null

        private val context: WaStickersOnline
            get() = instance as WaStickersOnline

        fun getContext(): Context = context
    }
}