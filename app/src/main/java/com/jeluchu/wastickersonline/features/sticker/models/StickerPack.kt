package com.jeluchu.wastickersonline.features.sticker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class StickerPack(
    val id: Int,
    val androidPlayStoreLink: String,
    val iosAppStoreLink: String,
    val publisherEmail: String,
    val privacyPolicyWebsite: String,
    val licenseAgreementWebsite: String,
    val telegramUrl: String,
    val identifier: Int,
    val name: String,
    val publisher: String,
    val publisherWebsite: String,
    val animatedStickerPack: Boolean,
    val stickers: List<Sticker>,
    val trayImageFile: String
): Parcelable {
    fun toStickerPackEntity(): StickerPackEntity =
        StickerPackEntity(
            id = id,
            androidPlayStoreLink = androidPlayStoreLink,
            iosAppStoreLink = iosAppStoreLink,
            publisherEmail = publisherEmail,
            privacyPolicyWebsite = privacyPolicyWebsite,
            licenseAgreementWebsite = licenseAgreementWebsite,
            telegramUrl = telegramUrl,
            identifier = identifier,
            name = name,
            publisher = publisher,
            publisherWebsite = publisherWebsite,
            animatedStickerPack = animatedStickerPack,
            stickers = stickers.map { it.toStickerEntity() },
            trayImageFile = trayImageFile
        )
}