package com.jeluchu.wastickersonline.features.stickerlist.models

data class Sticker(
    val emojis: List<String>?,
    val imageFile: String?,
    val imageFileThum: String?
) {

    fun toStickerView(): StickerView =
        StickerView(
            emojis ?: emptyList(),
            imageFile ?: "",
            imageFileThum ?: ""
        )

    companion object {
        fun empty() =
            Sticker(
                emptyList(),
                "",
                ""
            )
    }

}