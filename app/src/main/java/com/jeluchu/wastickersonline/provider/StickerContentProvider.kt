package com.jeluchu.wastickersonline.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.text.TextUtils
import android.util.Log
import com.jeluchu.wastickersonline.BuildConfig
import com.jeluchu.wastickersonline.StickerPack
import com.orhanobut.hawk.Hawk
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class StickerContentProvider : ContentProvider() {
    private val stickerPackList: MutableList<StickerPack> = ArrayList()
    override fun onCreate(): Boolean {
        Hawk.init(context).build()
        val authority = BuildConfig.CONTENT_PROVIDER_AUTHORITY
        check(authority.startsWith(Objects.requireNonNull(context).packageName)) { "your authority (" + authority + ") for the content provider should start with your package name: " + context!!.packageName }

        //the call to get the metadata for the sticker packs.
        MATCHER.addURI(authority, METADATA, METADATA_CODE)

        //the call to get the metadata for single sticker pack. * represent the identifier
        MATCHER.addURI(authority, "$METADATA/*", METADATA_CODE_FOR_SINGLE_PACK)

        //gets the list of stickers for a sticker pack, * respresent the identifier.
        MATCHER.addURI(authority, "$STICKERS/*", STICKERS_CODE)
        for (stickerPack in getStickerPackList()) {
            Log.e(TAG, "onCreate: " + stickerPack.identifier)
            MATCHER.addURI(authority, STICKERS_ASSET + "/" + stickerPack.identifier + "/" + stickerPack.trayImageFile, STICKER_PACK_TRAY_ICON_CODE)
            if (stickerPack.stickers != null) {
                for (sticker in stickerPack.stickers) {
                    MATCHER.addURI(authority, STICKERS_ASSET + "/" + stickerPack.identifier + "/" + sticker.imageFileName, STICKERS_ASSET_CODE)
                }
            }
        }
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val code = MATCHER.match(uri)
        Log.d(TAG, "query: $code$uri")
        return if (code == METADATA_CODE) {
            getPackForAllStickerPacks(uri)
        } else if (code == METADATA_CODE_FOR_SINGLE_PACK) {
            getCursorForSingleStickerPack(uri)
        } else if (code == STICKERS_CODE) {
            getStickersForAStickerPack(uri)
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    @Throws(FileNotFoundException::class)
    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        MATCHER.match(uri)
        val matchCode = MATCHER.match(uri)
        val pathSegments = uri.pathSegments
        Log.d(TAG, """
     openFile: $matchCode$uri
     ${uri.authority}
     ${pathSegments[pathSegments.size - 3]}/
     ${pathSegments[pathSegments.size - 2]}/
     ${pathSegments[pathSegments.size - 1]}
     """.trimIndent())
        return getImageAsset(uri)
    }

    @Throws(IllegalArgumentException::class)
    private fun getImageAsset(uri: Uri): AssetFileDescriptor? {
        val am = Objects.requireNonNull(context).assets
        val pathSegments = uri.pathSegments
        require(pathSegments.size == 3) { "path segments should be 3, uri is: $uri" }
        val fileName = pathSegments[pathSegments.size - 1]
        val identifier = pathSegments[pathSegments.size - 2]
        require(!TextUtils.isEmpty(identifier)) { "identifier is empty, uri: $uri" }
        require(!TextUtils.isEmpty(fileName)) { "file name is empty, uri: $uri" }
        //making sure the file that is trying to be fetched is in the list of stickers.
        for (stickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier) {
                if (fileName == stickerPack.trayImageFile) {
                    return fetchFile(uri, am, fileName, identifier)
                } else {
                    for (sticker in stickerPack.stickers) {
                        if (fileName == sticker.imageFileName) {
                            return fetchFile(uri, am, fileName, identifier)
                        }
                    }
                }
            }
        }
        return null
    }

    private fun fetchFile(uri: Uri, am: AssetManager, fileName: String, identifier: String): AssetFileDescriptor? {
        return try {
            val file: File
            file = if (fileName.endsWith(".png")) {
                File(context!!.filesDir.toString() + "/" + "stickers_asset" + "/" + identifier + "/try/", fileName)
            } else {
                File(context!!.filesDir.toString() + "/" + "stickers_asset" + "/" + identifier + "/", fileName)
            }
            if (!file.exists()) {
                Log.d("fetFile", "StickerPack dir not found")
            }
            Log.d("fetchFile", "StickerPack " + file.path)
            AssetFileDescriptor(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY), 0L, -1L)
        } catch (e: IOException) {
            Log.e(Objects.requireNonNull(context).packageName, "IOException when getting asset file, uri:$uri", e)
            null
        }
    }

    override fun getType(uri: Uri): String? {
        val matchCode = MATCHER.match(uri)
        return when (matchCode) {
            METADATA_CODE -> "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA
            METADATA_CODE_FOR_SINGLE_PACK -> "vnd.android.cursor.item/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA
            STICKERS_CODE -> "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + STICKERS
            STICKERS_ASSET_CODE -> "image/webp"
            STICKER_PACK_TRAY_ICON_CODE -> "image/png"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    @Synchronized
    private fun readContentFile(context: Context) {
        if (Hawk.get("sticker_pack", ArrayList<StickerPack>()) != null) {
            stickerPackList.addAll(Hawk.get("sticker_pack", ArrayList()))
        }
    }

    fun getStickerPackList(): List<StickerPack> {
        /* if (stickerPackList == null) {
            readContentFile(Objects.requireNonNull(getContext()));
        }*/
        return Hawk.get("sticker_packs", ArrayList<StickerPack>()) as List<*>
    }

    private fun getPackForAllStickerPacks(uri: Uri): Cursor {
        return getStickerPackInfo(uri, getStickerPackList())
    }

    private fun getCursorForSingleStickerPack(uri: Uri): Cursor {
        val identifier = uri.lastPathSegment
        for (stickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier) {
                return getStickerPackInfo(uri, listOf(stickerPack))
            }
        }
        return getStickerPackInfo(uri, ArrayList())
    }

    private fun getStickerPackInfo(uri: Uri, stickerPackList: List<StickerPack>): Cursor {
        val cursor = MatrixCursor(arrayOf(
                STICKER_PACK_IDENTIFIER_IN_QUERY,
                STICKER_PACK_NAME_IN_QUERY,
                STICKER_PACK_PUBLISHER_IN_QUERY,
                STICKER_PACK_ICON_IN_QUERY,
                ANDROID_APP_DOWNLOAD_LINK_IN_QUERY,
                IOS_APP_DOWNLOAD_LINK_IN_QUERY,
                PUBLISHER_EMAIL,
                PUBLISHER_WEBSITE,
                PRIVACY_POLICY_WEBSITE,
                LICENSE_AGREENMENT_WEBSITE
        ))
        for (stickerPack in stickerPackList) {
            val builder = cursor.newRow()
            builder.add(stickerPack.identifier)
            builder.add(stickerPack.name)
            builder.add(stickerPack.publisher)
            builder.add(stickerPack.trayImageFile)
            builder.add(stickerPack.androidPlayStoreLink)
            builder.add(stickerPack.iosAppStoreLink)
            builder.add(stickerPack.publisherEmail)
            builder.add(stickerPack.publisherWebsite)
            builder.add(stickerPack.privacyPolicyWebsite)
            builder.add(stickerPack.licenseAgreementWebsite)
        }
        Log.d(TAG, "getStickerPackInfo: " + stickerPackList.size)
        cursor.setNotificationUri(Objects.requireNonNull(context).contentResolver, uri)
        return cursor
    }

    private fun getStickersForAStickerPack(uri: Uri): Cursor {
        val identifier = uri.lastPathSegment
        val cursor = MatrixCursor(arrayOf(STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY))
        for (stickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier) {
                for (sticker in stickerPack.stickers) {
                    cursor.addRow(arrayOf<Any>(sticker.imageFileName, TextUtils.join(",", sticker.emojis)))
                }
            }
        }
        cursor.setNotificationUri(Objects.requireNonNull(context).contentResolver, uri)
        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not supported")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Not supported")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not supported")
    }

    companion object {
        /**
         * Do not change the strings listed below, as these are used by WhatsApp. And changing these will break the interface between sticker app and WhatsApp.
         */
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
        const val STICKER_FILE_NAME_IN_QUERY = "sticker_file_name"
        const val STICKER_FILE_EMOJI_IN_QUERY = "sticker_emoji"
        const val CONTENT_FILE_NAME = "contents.json"
        const val CONTENT_SCHEME = "content"
        private val TAG = StickerContentProvider::class.java.simpleName
        var AUTHORITY_URI = Uri.Builder().scheme(CONTENT_SCHEME).authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath(METADATA).build()

        /**
         * Do not change the values in the UriMatcher because otherwise, WhatsApp will not be able to fetch the stickers from the ContentProvider.
         */
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