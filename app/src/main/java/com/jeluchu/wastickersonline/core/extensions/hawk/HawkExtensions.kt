package com.jeluchu.wastickersonline.core.extensions.hawk

import android.content.Context
import com.jeluchu.wastickersonline.core.utils.hawk.Hawk

fun Context.initHawk() = Hawk.init(this).build()