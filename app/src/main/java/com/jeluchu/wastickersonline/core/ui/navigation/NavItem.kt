package com.jeluchu.wastickersonline.core.ui.navigation

import android.net.Uri
import androidx.navigation.navArgument
import com.jeluchu.aruppi.core.ui.navigation.Feature
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

sealed class NavItem(
    internal val feature: Feature,
    private val arguments: List<NavArgs> = emptyList()
) {

    class ContentScreen(feature: Feature) : NavItem(feature)

    class ContentDetails(feature: Feature) : NavItem(feature, listOf(NavArgs.Sticker)) {
        fun createRoute(sticker: StickerPack) =
            "${feature.route}/${Uri.encode(Json.encodeToJsonElement(sticker).toString())}"
    }

    val route = run {
        val argValues = arguments.map { "{${it.key}}" }
        listOf(feature.route)
            .plus(argValues)
            .joinToString("/")
    }

    val args = arguments.map {
        navArgument(it.key) { }
    }
}

enum class NavArgs(
    val key: String
) { Sticker("Sticker") }