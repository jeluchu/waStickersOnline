package com.jeluchu.wastickersonline.features.stickerlist.models

data class StickerPack(
        val telegram_url: String?,
        val identifier: Int?,
        val name: String?,
        val publisher: String?,
        val publisherWebsite: String?,
        val stickers: List<Sticker>?,
        val trayImageFile: String?
) {

    fun toStickersPackView(): StickerPackView =
        StickerPackView(
            telegram_url ?: "",
            identifier ?: 0,
            name ?: "",
            publisher ?: "",
            publisherWebsite ?: "",
            stickers?.map { it.toStickerView() } ?: emptyList(),
            trayImageFile ?: ""
        )

    companion object {
        fun empty() =
            StickerPack(
                "",
                0,
                "",
                "",
                "",
                emptyList(),
                ""
            )
    }

}