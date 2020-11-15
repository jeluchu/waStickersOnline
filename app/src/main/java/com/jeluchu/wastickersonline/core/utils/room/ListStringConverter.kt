package com.jeluchu.wastickersonline.core.utils.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jeluchu.wastickersonline.features.sticker.models.StickerEntity
import java.lang.reflect.Type
import java.util.*

class ListStringConverter {

    private val gson = Gson()

    @TypeConverter
    fun stringToListSticker(data: String?): List<StickerEntity?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object :
                TypeToken<List<StickerEntity?>?>() {}.type
        return gson.fromJson<List<StickerEntity?>>(data, listType)
    }

    @TypeConverter
    fun listStickerToString(someObjects: List<StickerEntity?>?): String? {
        return gson.toJson(someObjects)
    }

}