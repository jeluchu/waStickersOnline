package com.jeluchu.wastickersonline.features.stickerlist.models


import com.google.gson.annotations.SerializedName

data class StickerEntity(
    @SerializedName("emojis")
    val emojis: List<String>?,
    @SerializedName("image_file")
    val imageFile: String?,
    @SerializedName("image_file_thum")
    val imageFileThum: String?
) {

    fun toStickers(): Sticker =
        Sticker(
            emojis,
            imageFile,
            imageFileThum
        )

    companion object {
        fun empty() =
            StickerEntity(
                emptyList(),
                "",
                ""
            )
    }

}