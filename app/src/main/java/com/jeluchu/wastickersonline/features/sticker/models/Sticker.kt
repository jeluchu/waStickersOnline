package com.jeluchu.wastickersonline.features.sticker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Sticker(
    val emojis: List<String>,
    val imageFile: String,
    val imageFileThum: String
): Parcelable {
    fun toStickerEntity(): StickerEntity =
        StickerEntity(
            emojis = emojis,
            imageFile = imageFile,
            imageFileThum = imageFileThum
        )
}