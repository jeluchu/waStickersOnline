package com.jeluchu.wastickersonline.core.extensions.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack

class SharedPrefsHelpers {

    fun saveLong(key: String?, value: Long) {
        val editor = mSharedPreferences!!.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return if (isKeyExists(key)) {
            mSharedPreferences!!.getLong(key, defaultValue)
        } else defaultValue
    }

    fun <T> saveObjectsList(key: String?, objectList: List<T>?) {
        val objectString = Gson().toJson(objectList)
        val editor = mSharedPreferences!!.edit()
        editor.putString(key, objectString)
        editor.apply()
    }

    fun getObjectsStickerPackViewList(key: String): List<StickerPack> {
        if (isKeyExists(key)) {
            val objectString = mSharedPreferences!!.getString(key, null)
            if (objectString != null) {
                val t: ArrayList<StickerPack> =
                    Gson().fromJson(
                        objectString,
                        object : TypeToken<List<StickerPack>?>() {}.type
                    )
                val finalList: MutableList<StickerPack> = ArrayList()
                for (i in 0 until t.size) {
                    finalList.add(
                        StickerPack(
                            t[i].id,
                            t[i].androidPlayStoreLink,
                            t[i].iosAppStoreLink,
                            t[i].publisherEmail,
                            t[i].privacyPolicyWebsite,
                            t[i].licenseAgreementWebsite,
                            t[i].telegramUrl,
                            t[i].identifier,
                            t[i].name,
                            t[i].publisher,
                            t[i].publisherWebsite,
                            t[i].animatedStickerPack,
                            t[i].stickers,
                            t[i].trayImageFile
                        )
                    )
                }
                return finalList
            }
        }
        return emptyList()
    }

    private fun isKeyExists(key: String): Boolean {
        val map = mSharedPreferences!!.all
        return if (map.containsKey(key)) {
            true
        } else {
            Log.e("SharedPreferences", "No element founded in sharedPrefs with the key $key")
            false
        }
    }

    companion object {
        private var mSharedPreferences: SharedPreferences? = null

        fun init(context: Context?) {
            mSharedPreferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        }
    }
}