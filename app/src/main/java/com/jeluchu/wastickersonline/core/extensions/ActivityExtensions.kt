package com.jeluchu.wastickersonline.core.extensions

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.jeluchu.wastickersonline.BuildConfig
import com.jeluchu.wastickersonline.core.utils.Stickers
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack

val Activity.permissionStorage: Unit
    get() {
        val perm =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (perm != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                1
            )
        }
    }

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
            "No se añadió el paquete de stickers. Si deseas añadirlo, instala o actualiza WhatsApp.",
            Toast.LENGTH_LONG
        ).show()
    }
}