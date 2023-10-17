package com.jeluchu.wastickersonline.core.ui.navigation

import android.net.Uri
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

sealed class Screen(val route: String) {
    data object Dashboard : Screen("login")
    data object Detail : Screen("detail/{${NavArgs.Sticker.key}}") {
        fun createRoute(sticker: StickerPack) =
            "detail/${Uri.encode(Json.encodeToJsonElement(sticker).toString())}"
    }
}

enum class NavArgs(val key: String) {
    Sticker("sticker")
}