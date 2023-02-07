package com.jeluchu.wastickersonline.features.sticker.models

import android.os.Parcelable
import java.io.Serializable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StickerView(
    val emojis: List<String>,
    val imageFile: String,
    val imageFileThum: String
) : Serializable, Parcelable