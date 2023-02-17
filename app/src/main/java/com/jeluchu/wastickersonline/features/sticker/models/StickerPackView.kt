package com.jeluchu.wastickersonline.features.sticker.models

import android.os.Parcelable
import java.io.Serializable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StickerPackView(
    val androidPlayStoreLink: String?,
    val iosAppStoreLink: String?,
    val publisherEmail: String?,
    val privacyPolicyWebsite: String?,
    val licenseAgreementWebsite: String?,
    val telegram_url: String,
    val identifier: Int,
    val name: String,
    val publisher: String,
    val publisherWebsite: String,
    val animatedStickerPack: Boolean,
    val stickers: List<StickerView>,
    val trayImageFile: String
) : Serializable, Parcelable