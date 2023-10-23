package com.jeluchu.wastickersonline.core.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import com.jeluchu.wastickersonline.BuildConfig
import com.jeluchu.wastickersonline.R
import com.jeluchu.wastickersonline.core.utils.Stickers
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack

fun Activity.addStickers(stickerPack: StickerPack) {
    val intent = Intent().apply {
        action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
        putExtra(Stickers.packId, stickerPack.identifier.toString())
        putExtra(Stickers.authority, BuildConfig.CONTENT_PROVIDER_AUTHORITY)
        putExtra(Stickers.packName, stickerPack.name)
    }
    try {
        startActivityForResult(intent, 200)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            this,
            getString(R.string.not_added_to_whatsapp),
            Toast.LENGTH_LONG
        ).show()
    }
}