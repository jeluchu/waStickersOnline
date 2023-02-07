package com.jeluchu.wastickersonline.core.extensions.others

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Browser
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.jeluchu.wastickersonline.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

inline val buildIsMarshmallowAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

inline val buildIsLollipopAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1

fun Activity.statusBarColor() {
    setStatusBarColor(R.color.white)
    setSystemBarLight(this)
}

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
fun Activity.setStatusBarColor(@ColorRes color: Int) {
    if (buildIsLollipopAndUp) {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, color)
    }
}

fun setSystemBarLight(act: Activity) {
    if (buildIsMarshmallowAndUp) {
        val view = act.findViewById<View>(android.R.id.content)
        var flags = view.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        view.systemUiVisibility = flags
    }
}

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun Activity.openActivityRight() {
    overridePendingTransition(R.anim.enter_slide_right, R.anim.exit_slide_left)
}

fun Activity.exitActivityBottom() {
    finish()
    overridePendingTransition(R.anim.enter_slide_bottom, R.anim.exit_slide_bottom)
}

fun Activity.exitActivityLeft() {
    finish()
    overridePendingTransition(R.anim.enter_slide_left, R.anim.exit_slide_right)
}

fun Activity.openInCustomTab(string: String) = customTabsWeb(string)

private fun Context.customTabsWeb(string: String) {
    try {

        val builder = CustomTabsIntent.Builder()

        builder.setToolbarColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(this, R.color.redCardBackground))))
        builder.setShowTitle(true)
        builder.setExitAnimations(this, R.anim.enter_slide_left, R.anim.exit_slide_left)
        builder.setStartAnimations(this, R.anim.enter_slide_right, R.anim.exit_slide_right)

        val intent = builder.build()
        intent.launchUrl(this, Uri.parse(string))

    }
    catch (e: IOException) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(string))
        intent.putExtra(Browser.EXTRA_CREATE_NEW_TAB, true)
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, packageName)
        startActivity(intent)
    }
}

fun noCrash(enableLog: Boolean = true, func: () -> Unit): String? {
    return try {
        func()
        null
    } catch (e: Exception) {
        if (enableLog)
            e.printStackTrace()
        e.message
    }
}

fun TextView.simpletext(value: String) {
    this.text = value
}

fun String.getLastBitFromUrl(): String = replaceFirst(".*/([^/?]+).*".toRegex(), "$1")

fun String.saveImage(destinationFile: File) {
    runCatching {
        Thread {
            val url = URL(this)
            val inputStream = url.openStream()
            val os = FileOutputStream(destinationFile)
            val b = ByteArray(2048)
            var length: Int
            while (inputStream.read(b).also { length = it } != -1) {
                os.write(b, 0, length)
            }
            inputStream?.close()
            os.close()
        }.start()

    }.getOrElse {
        it.printStackTrace()
    }
}

fun ViewGroup.inflate(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

class ContextHandler
    (private val context: Context) {
    val appContext: Context get() = context.applicationContext
}

inline val buildIsMAndLower: Boolean
    get() = Build.VERSION.SDK_INT <= Build.VERSION_CODES.M

@SuppressLint("NewApi")
fun Context.checkNetworkState(): Boolean {
    val connectivityManager =
            this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (!buildIsMAndLower) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        val nw = connectivityManager.activeNetworkInfo ?: return false
        return when (nw.type) {
            (NetworkCapabilities.TRANSPORT_WIFI) -> true
            (NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

}