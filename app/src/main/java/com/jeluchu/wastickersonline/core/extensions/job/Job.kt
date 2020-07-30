package com.jeluchu.wastickersonline.core.extensions.job

import kotlinx.coroutines.Job

fun Job?.cancelIfActive() {
    if (this?.isActive == true) {
        cancel()
    }
}