package com.jeluchu.wastickersonline.core.extensions.sharedprefs

import android.content.Context

fun Context.initSharedPrefs() = SharedPrefsHelpers.init(this)