package com.jeluchu.wastickersonline.features.stickerlist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class StickerView(
    val emojis: List<String>,
    val imageFile: String,
    val imageFileThum: String
) : Serializable, Parcelable {

    companion object {
        fun empty(): StickerView =
            StickerView(
                emptyList(),
                "",
                ""
            )
    }

}