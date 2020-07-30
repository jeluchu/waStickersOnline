package com.jeluchu.wastickersonline.core.platform

import android.content.Context
import com.jeluchu.wastickersonline.core.extensions.context.checkNetworkState

class NetworkHandler
(private val context: Context) {
    val isConnected get() = context.checkNetworkState()
}