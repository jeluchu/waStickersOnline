package com.jeluchu.wastickersonline.features.sticker.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.jeluchu.jchucomponents.ktx.bool.orFalse
import com.jeluchu.jchucomponents.ktx.numbers.orEmpty

@Entity
data class StickerPackEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @SerializedName("telegram_url")
    val telegramUrl: String?,

    @SerializedName("publisher_website")
    val publisherWebsite: String?,

    @SerializedName("animated_sticker_pack")
    val animatedStickerPack: Boolean?,

    @SerializedName("tray_image_file")
    val trayImageFile: String?,

    val androidPlayStoreLink: String?,
    val iosAppStoreLink: String?,
    val publisherEmail: String?,
    val privacyPolicyWebsite: String?,
    val licenseAgreementWebsite: String?,
    val identifier: Int?,
    val name: String?,
    val publisher: String?,
    val stickers: List<StickerEntity>?,
) {
    fun toStickersPack(): StickerPack =
        StickerPack(
            id = id,
            androidPlayStoreLink = androidPlayStoreLink.orEmpty(),
            iosAppStoreLink = iosAppStoreLink.orEmpty(),
            publisherEmail = publisherEmail.orEmpty(),
            privacyPolicyWebsite = privacyPolicyWebsite.orEmpty(),
            licenseAgreementWebsite = licenseAgreementWebsite.orEmpty(),
            telegramUrl = telegramUrl.orEmpty(),
            identifier = identifier.orEmpty(),
            name = name.orEmpty(),
            publisher = publisher.orEmpty(),
            publisherWebsite = publisherWebsite.orEmpty(),
            animatedStickerPack = animatedStickerPack.orFalse(),
            stickers = stickers?.map { it.toStickers() }.orEmpty(),
            trayImageFile = trayImageFile.orEmpty()
        )
}