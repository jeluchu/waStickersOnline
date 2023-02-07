package com.jeluchu.wastickersonline.core.extensions

import android.content.Intent
import android.os.Bundle
import com.jeluchu.jchucomponents.ktx.packageutils.buildIsTiramisuAndUp
import java.io.Serializable

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    buildIsTiramisuAndUp -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    buildIsTiramisuAndUp -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}