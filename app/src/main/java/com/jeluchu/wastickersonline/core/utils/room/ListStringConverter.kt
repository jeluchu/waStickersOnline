package com.jeluchu.wastickersonline.core.utils.room

import androidx.room.TypeConverter
import com.jeluchu.jchucomponents.ktx.gson.fromJsonList
import com.jeluchu.jchucomponents.ktx.gson.gson
import com.jeluchu.jchucomponents.ktx.gson.toJson
import com.jeluchu.wastickersonline.features.sticker.models.StickerEntity

class ListStringConverter {

    @TypeConverter
    fun stringToListSticker(data: String?): List<StickerEntity?>? = gson.fromJsonList(data)

    @TypeConverter
    fun listStickerToString(data: List<StickerEntity?>?): String? = data.toJson()

}