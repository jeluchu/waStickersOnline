package com.jeluchu.wastickersonline.features.sticker.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class StickerEntity(
    @SerializedName("emojis")
    val emojis: List<String>?,
    @SerializedName("image_file")
    val imageFile: String?,
    @SerializedName("image_file_thum")
    val imageFileThum: String?
): Parcelable {
    fun toStickers(): Sticker =
        Sticker(
            emojis = emojis.orEmpty(),
            imageFile = imageFile.orEmpty(),
            imageFileThum = imageFileThum.orEmpty()
        )
}