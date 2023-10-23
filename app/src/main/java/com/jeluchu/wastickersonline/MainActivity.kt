package com.jeluchu.wastickersonline

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jeluchu.wastickersonline.core.ui.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        initRequiresConfig()

        setContent {
            MaterialTheme {
                Navigation()
            }
        }
    }

    private fun initRequiresConfig() {
        path = "$filesDir/stickers_asset"
    }

    private fun setupSplashScreen() {
        installSplashScreen().setKeepOnScreenCondition { false }
    }

    companion object {
        @JvmField
        var path: String? = null
    }
}