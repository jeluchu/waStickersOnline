package com.jeluchu.wastickersonline.features.stickerlist.models


import com.google.gson.annotations.SerializedName

data class StickerPackEntity(
        @SerializedName("androidPlayStoreLink")
        val androidPlayStoreLink: String?,
        @SerializedName("iosAppStoreLink")
        val iosAppStoreLink: String?,
        @SerializedName("publisherEmail")
        val publisherEmail: String?,
        @SerializedName("privacyPolicyWebsite")
        val privacyPolicyWebsite: String?,
        @SerializedName("licenseAgreementWebsite")
        val licenseAgreementWebsite: String?,
        @SerializedName("telegram_url")
        val telegram_url: String?,
        @SerializedName("identifier")
        val identifier: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("publisher")
        val publisher: String?,
        @SerializedName("publisher_website")
        val publisherWebsite: String?,
        @SerializedName("stickers")
        val stickers: List<StickerEntity>?,
        @SerializedName("tray_image_file")
        val trayImageFile: String?
) {

    fun toStickersPack(): StickerPack =
            StickerPack(
                    androidPlayStoreLink ?: "",
                    iosAppStoreLink ?: "",
                    publisherEmail ?: "",
                    privacyPolicyWebsite ?: "",
                    licenseAgreementWebsite ?: "",
                    telegram_url ?: "",
                    identifier ?: 0,
                    name ?: "",
                    publisher ?: "",
                    publisherWebsite ?: "",
                    stickers?.map { it.toStickers() } ?: emptyList(),
                    trayImageFile ?: ""
            )

    companion object {
        fun empty() =
                StickerPackEntity(
                        "",
                        "",
                        "",
                        "",
                        "",
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