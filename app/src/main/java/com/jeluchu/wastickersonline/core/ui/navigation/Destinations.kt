package com.jeluchu.aruppi.core.ui.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.jeluchu.jchucomponents.ktx.navigation.lifecycleIsResumed
import com.jeluchu.wastickersonline.core.ui.navigation.NavItem
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack

class Destinations(private val navController: NavHostController) {

    private fun String.navigate() = navController.navigate(this)

    val goToDashboard: () -> Unit = { Feature.DASHBOARD.route.navigate() }
    val goToDetails: (StickerPack) -> Unit = { sticker ->
        navController.navigate(NavItem.ContentDetails(Feature.DETAILS).createRoute(sticker))
    }

    val goBack: (from: NavBackStackEntry) -> Unit = { from ->
        if (from.lifecycleIsResumed()) navController.popBackStack()
    }
}