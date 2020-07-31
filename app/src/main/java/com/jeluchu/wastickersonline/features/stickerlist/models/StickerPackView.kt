package com.jeluchu.wastickersonline.features.stickerlist.models

import java.io.Serializable

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
        val stickers: List<StickerView>,
        val trayImageFile: String
): Serializable