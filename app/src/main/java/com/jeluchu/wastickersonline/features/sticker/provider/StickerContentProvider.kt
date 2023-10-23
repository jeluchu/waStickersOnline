package com.jeluchu.wastickersonline.features.sticker.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.text.TextUtils
import android.util.Log
import com.jeluchu.jchucomponents.ktx.strings.getLastBitFromUrl
import com.jeluchu.wastickersonline.BuildConfig
import com.jeluchu.wastickersonline.core.extensions.sharedprefs.SharedPrefsHelpers
import com.jeluchu.wastickersonline.core.extensions.sharedprefs.initSharedPrefs
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class StickerContentProvider : ContentProvider() {

    private val preferences by lazy { SharedPrefsHelpers() }

    override fun onCreate(): Boolean {
        context?.initSharedPrefs()

        val authority = BuildConfig.CONTENT_PROVIDER_AUTHORITY
        check(authority.startsWith(context!!.packageName)) { "your authority (" + authority + ") for the content provider should start with your package name: " + context!!.packageName }

        MATCHER.addURI(authority, METADATA, METADATA_CODE)
        MATCHER.addURI(authority, "$METADATA/*", METADATA_CODE_FOR_SINGLE_PACK)
        MATCHER.addURI(authority, "$STICKERS/*", STICKERS_CODE)

        for (stickerPack in getStickerPackList()) {
            MATCHER.addURI(
                authority,
                STICKERS_ASSET + "/" + stickerPack.identifier + "/" + stickerPack.trayImageFile.getLastBitFromUrl(),
                STICKER_PACK_TRAY_ICON_CODE
            )
            for (sticker in stickerPack.stickers) {
                MATCHER.addURI(
                    authority,
                    STICKERS_ASSET + "/" + stickerPack.identifier + "/" + sticker.imageFile.getLastBitFromUrl(),
                    STICKERS_ASSET_CODE
                )
            }
        }
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor =
        when (MATCHER.match(uri)) {
            METADATA_CODE -> getPackForAllStickerPacks(uri)
            METADATA_CODE_FOR_SINGLE_PACK -> getCursorForSingleStickerPack(uri)
            STICKERS_CODE -> getStickersForAStickerPack(uri)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

    @Throws(FileNotFoundException::class)
    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        MATCHER.match(uri)
        val matchCode = MATCHER.match(uri)
        val pathSegments = uri.pathSegments
        Log.d(
            TAG, """
     openFile: $matchCode$uri
     ${uri.authority}
     ${pathSegments[pathSegments.size - 3]}/
     ${pathSegments[pathSegments.size - 2]}/
     ${pathSegments[pathSegments.size - 1]}
     """.trimIndent()
        )
        return getImageAsset(uri)
    }

    @Throws(IllegalArgumentException::class)
    private fun getImageAsset(uri: Uri): AssetFileDescriptor? {

        val pathSegments = uri.pathSegments
        require(pathSegments.size == 3) { "path segments should be 3, uri is: $uri" }

        val fileName = pathSegments[pathSegments.size - 1]
        val identifier = pathSegments[pathSegments.size - 2]
        require(!TextUtils.isEmpty(identifier)) { "identifier is empty, uri: $uri" }
        require(!TextUtils.isEmpty(fileName)) { "file name is empty, uri: $uri" }

        for (stickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier.toString()) {
                if (fileName == stickerPack.trayImageFile.getLastBitFromUrl()) {
                    return fetchFile(uri, fileName, identifier)
                } else {
                    for (sticker in stickerPack.stickers) {
                        if (fileName == sticker.imageFile.getLastBitFromUrl()) {
                            return fetchFile(uri, fileName, identifier)
                        }
                    }
                }
            }
        }
        return null
    }

    private fun fetchFile(uri: Uri, fileName: String, identifier: String): AssetFileDescriptor? {
        return try {
            val file: File = if (fileName.endsWith(".png")) {
                File(
                    context!!.filesDir.toString() + "/" + "stickers_asset" + "/" + identifier + "/try/",
                    fileName
                )
            } else {
                File(
                    context!!.filesDir.toString() + "/" + "stickers_asset" + "/" + identifier + "/",
                    fileName
                )
            }
            if (!file.exists()) {
                Log.d("fetFile", "StickerPack dir not found")
            }
            Log.d("fetchFile", "StickerPack " + file.path)
            AssetFileDescriptor(
                ParcelFileDescriptor.open(
                    file,
                    ParcelFileDescriptor.MODE_READ_ONLY
                ), 0L, -1L
            )
        } catch (e: IOException) {
            Log.e(context!!.packageName, "IOException when getting asset file, uri:$uri", e)
            null
        }
    }

    override fun getType(uri: Uri): String =
        when (MATCHER.match(uri)) {
            METADATA_CODE -> "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA
            METADATA_CODE_FOR_SINGLE_PACK -> "vnd.android.cursor.item/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA
            STICKERS_CODE -> "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + STICKERS
            STICKERS_ASSET_CODE -> "image/png"
            STICKER_PACK_TRAY_ICON_CODE -> "image/png"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

    private fun getStickerPackList(): List<StickerPack> =
        preferences.getObjectsStickerPackViewList("sticker_packs")

    private fun getPackForAllStickerPacks(uri: Uri): Cursor =
        getStickerPackInfo(uri, getStickerPackList())

    private fun getCursorForSingleStickerPack(uri: Uri): Cursor {
        val identifier = uri.lastPathSegment
        for (stickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier.toString()) {
                return getStickerPackInfo(uri, listOf(stickerPack))
            }
        }
        return getStickerPackInfo(uri, ArrayList())
    }

    private fun getStickerPackInfo(uri: Uri, stickerPackList: List<StickerPack>): Cursor {
        val cursor = MatrixCursor(
            arrayOf(
                STICKER_PACK_IDENTIFIER_IN_QUERY,
                STICKER_PACK_NAME_IN_QUERY,
                STICKER_PACK_PUBLISHER_IN_QUERY,
                STICKER_PACK_ICON_IN_QUERY,
                ANDROID_APP_DOWNLOAD_LINK_IN_QUERY,
                IOS_APP_DOWNLOAD_LINK_IN_QUERY,
                PUBLISHER_EMAIL,
                PUBLISHER_WEBSITE,
                PRIVACY_POLICY_WEBSITE,
                LICENSE_AGREENMENT_WEBSITE,
                ANIMATED_STICKER_PACK
            )
        )
        for (stickerPack in stickerPackList) {
            val builder = cursor.newRow()
            builder.add(stickerPack.identifier)
            builder.add(stickerPack.name)
            builder.add(stickerPack.publisher)
            builder.add(stickerPack.trayImageFile.getLastBitFromUrl())
            builder.add(stickerPack.androidPlayStoreLink)
            builder.add(stickerPack.iosAppStoreLink)
            builder.add(stickerPack.publisherEmail)
            builder.add(stickerPack.publisherWebsite)
            builder.add(stickerPack.privacyPolicyWebsite)
            builder.add(stickerPack.licenseAgreementWebsite)
            builder.add(if (stickerPack.animatedStickerPack) 1 else 0)

        }
        Log.d(TAG, "getStickerPackInfo: " + stickerPackList.size)
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    private fun getStickersForAStickerPack(uri: Uri): Cursor {
        val identifier = uri.lastPathSegment
        val cursor = MatrixCursor(arrayOf(STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY))
        for (stickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier.toString()) {
                for (sticker in stickerPack.stickers) {
                    cursor.addRow(
                        arrayOf<Any>(
                            sticker.imageFile.getLastBitFromUrl(),
                            TextUtils.join(",", sticker.emojis)
                        )
                    )
                }
            }
        }
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int =
        throw UnsupportedOperationException("Not supported")

    override fun insert(uri: Uri, values: ContentValues?): Uri =
        throw UnsupportedOperationException("Not supported")

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int =
        throw UnsupportedOperationException("Not supported")

    private

    companion object {
        const val STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier"
        const val STICKER_PACK_NAME_IN_QUERY = "sticker_pack_name"
        const val STICKER_PACK_PUBLISHER_IN_QUERY = "sticker_pack_publisher"
        const val STICKER_PACK_ICON_IN_QUERY = "sticker_pack_icon"
        const val ANDROID_APP_DOWNLOAD_LINK_IN_QUERY = "android_play_store_link"
        const val IOS_APP_DOWNLOAD_LINK_IN_QUERY = "ios_app_download_link"
        const val PUBLISHER_EMAIL = "sticker_pack_publisher_email"
        const val PUBLISHER_WEBSITE = "sticker_pack_publisher_website"
        const val PRIVACY_POLICY_WEBSITE = "sticker_pack_privacy_policy_website"
        const val LICENSE_AGREENMENT_WEBSITE = "sticker_pack_license_agreement_website"
        const val ANIMATED_STICKER_PACK = "animated_sticker_pack"
        const val STICKER_FILE_NAME_IN_QUERY = "sticker_file_name"
        const val STICKER_FILE_EMOJI_IN_QUERY = "sticker_emoji"
        private val TAG = StickerContentProvider::class.java.simpleName
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)
        const val METADATA = "metadata"
        private const val METADATA_CODE = 1
        private const val METADATA_CODE_FOR_SINGLE_PACK = 2
        const val STICKERS = "stickers"
        private const val STICKERS_CODE = 3
        const val STICKERS_ASSET = "stickers_asset"
        private const val STICKERS_ASSET_CODE = 4
        private const val STICKER_PACK_TRAY_ICON_CODE = 5

    }
}