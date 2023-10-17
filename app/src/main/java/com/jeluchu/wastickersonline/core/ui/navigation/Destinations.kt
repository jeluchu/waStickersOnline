package com.jeluchu.wastickersonline.core.ui.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.jeluchu.jchucomponents.ktx.navigation.lifecycleIsResumed
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack

class Destinations(private val navController: NavHostController) {

    private fun String.navigate() = navController.navigate(this)

    val goToDashboard: () -> Unit = { Screen.Dashboard.route.navigate() }
    val goToDetails: (StickerPack) -> Unit = { sticker ->
        navController.navigate(Screen.Detail.createRoute(sticker))
    }

    val goBack: (from: NavBackStackEntry) -> Unit = { from ->
        if (from.lifecycleIsResumed()) navController.popBackStack()
    }
}