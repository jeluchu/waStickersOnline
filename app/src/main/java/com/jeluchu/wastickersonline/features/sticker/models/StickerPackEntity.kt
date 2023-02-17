package com.jeluchu.wastickersonline.features.sticker.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class StickerPackEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @SerializedName("androidPlayStoreLink")
    @ColumnInfo(name = "androidPlayStoreLink")
    val androidPlayStoreLink: String?,

    @SerializedName("iosAppStoreLink")
    @ColumnInfo(name = "iosAppStoreLink")
    val iosAppStoreLink: String?,

    @SerializedName("publisherEmail")
    @ColumnInfo(name = "publisherEmail")
    val publisherEmail: String?,

    @SerializedName("privacyPolicyWebsite")
    @ColumnInfo(name = "privacyPolicyWebsite")
    val privacyPolicyWebsite: String?,

    @SerializedName("licenseAgreementWebsite")
    @ColumnInfo(name = "licenseAgreementWebsite")
    val licenseAgreementWebsite: String?,

    @SerializedName("telegram_url")
    @ColumnInfo(name = "telegram_url")
    val telegram_url: String?,

    @SerializedName("identifier")
    @ColumnInfo(name = "identifier")
    val identifier: Int?,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String?,

    @SerializedName("publisher")
    @ColumnInfo(name = "publisher")
    val publisher: String?,

    @SerializedName("publisher_website")
    @ColumnInfo(name = "publisher_website")
    val publisherWebsite: String?,

    @SerializedName("animated_sticker_pack")
    @ColumnInfo(name = "animated_sticker_pack")
    val animatedStickerPack: Boolean?,

    @SerializedName("stickers")
    @ColumnInfo(name = "stickers")
    val stickers: List<StickerEntity>?,

    @SerializedName("tray_image_file")
    @ColumnInfo(name = "tray_image_file")
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
            animatedStickerPack ?: false,
            stickers?.map { it.toStickers() } ?: emptyList(),
            trayImageFile ?: ""
        )

}